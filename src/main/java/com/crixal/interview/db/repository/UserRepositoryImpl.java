package com.crixal.interview.db.repository;

import com.crixal.interview.dto.UserDTO;
import com.crixal.interview.exception.BadInputDataException;
import com.crixal.interview.exception.NotEnoughMoneyException;
import com.crixal.interview.exception.UserNotFoundException;
import org.skife.jdbi.v2.Handle;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends RepositoryBase implements UserRepository {
    UserRepositoryImpl(Handle connection) {
        super(connection);
    }

    @Override
    public Collection<UserDTO> getUsers() {
        return connection
                .createQuery("SELECT * FROM user")
                .setFetchSize(1000)
                .map((index, r, ctx) -> fillUserDTO(r))
                .list();
    }

    @Override
    public boolean transfer(Long fromId, Long toId, BigDecimal money) {
        if (fromId == null || toId == null) {
            throw new BadInputDataException("Both from and to users should be not null");
        }

        if (fromId.equals(toId)) {
            throw new BadInputDataException(String.format("Transfer to same user id '%s'", fromId));
        }

        Map<Long, UserInfo> userInfos = connection.createQuery("SELECT id, balance, version FROM user WHERE id IN (:id1, :id2) FOR UPDATE")
                .bind("id1", fromId)
                .bind("id2", toId)
                .map((index, r, ctx) -> fillUserInfo(r))
                .list()
                .stream()
                .collect(Collectors.toMap(UserInfo::getId, u -> u));

        UserInfo fromUserInfo = userInfos.get(fromId);
        UserInfo toUserInfo = userInfos.get(toId);

        if (fromUserInfo == null) {
            throw new UserNotFoundException(String.format("User with id '%s' was not found", fromId));
        }

        if (toUserInfo == null) {
            throw new UserNotFoundException(String.format("User with id '%s' was not found", toId));
        }

        if (fromUserInfo.balance.subtract(money).compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException(fromId, fromUserInfo.balance, money);
        }

        return changeBalance(fromId, fromUserInfo.balance.subtract(money), fromUserInfo.version) == 1 &&
                changeBalance(toId, toUserInfo.balance.add(money), toUserInfo.version) == 1;
    }

    private int changeBalance(Long id, BigDecimal balance, Long version) {
        return connection.update("UPDATE user SET balance = :balance WHERE id = :id AND version = :version",
                balance, id, version);
    }


    private UserDTO fillUserDTO(ResultSet r) {
        try {
            return new UserDTO(
                    r.getLong("id"),
                    r.getString("name"),
                    r.getBigDecimal("balance"),
                    r.getLong("version"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserInfo fillUserInfo(ResultSet r) {
        try {
            return new UserInfo(
                    r.getLong("id"),
                    r.getBigDecimal("balance"),
                    r.getLong("version"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    class UserInfo {
        private Long id;
        private BigDecimal balance;
        private Long version;

        UserInfo(Long id, BigDecimal balance, Long version) {
            this.id = id;
            this.balance = balance;
            this.version = version;
        }

        Long getId() {
            return id;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public Long getVersion() {
            return version;
        }
    }
}
