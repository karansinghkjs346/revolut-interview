package com.crixal.interview.service;

import com.crixal.interview.db.repository.RepositoryFactory;
import com.crixal.interview.db.repository.UserRepository;
import com.crixal.interview.dto.UserDTO;
import com.crixal.interview.exception.RetryOperationException;
import org.h2.jdbc.JdbcSQLException;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Supplier;

public class UserServiceImpl implements UserService {
    private final RepositoryFactory repositoryFactory;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public Collection<UserDTO> getUsers() {
        try (UserRepository repository = repositoryFactory.getUserRepository()) {
            return repository.getUsers();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transfer(Long fromId, Long toId, BigDecimal money) {
        retry(fromId, toId, () -> {
            try (UserRepository repository = repositoryFactory.getUserRepository()) {
                repository.transfer(fromId, toId, money);
                repository.commit();
            } catch (UnableToExecuteStatementException e) {
                // trying to identify exception from DB lock operation
                if (e.getCause() != null && e.getCause() instanceof JdbcSQLException &&
                        e.getCause().getCause() != null && e.getCause().getCause() instanceof JdbcSQLException &&
                        e.getCause().getCause().getCause() != null && e.getCause().getCause().getCause() instanceof IllegalStateException) {
                    return false;
                } else {
                    log.error("Retry of money from user id '{}' to user id '{}' with money '{}' was failed", fromId, toId, money, e);
                    throw e;
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                log.error("Retry of money from user id '{}' to user id '{}' with money '{}' was failed", fromId, toId, money, e);
                throw new RuntimeException(e);
            }
            log.info("Transfer from user id {} to user id '{}' with money '{}' was successfully finished", fromId, toId, money);
            return true;
        });
    }

    private void retry(Long fromId, Long toId, Supplier<Boolean> supplier) {
        try {
            int counter = 0;
            while (counter < 10) {
                if (supplier.get()) {
                    return;
                } else {
                    counter++;
                    log.info("Retrying from user id '{} to user id '{}': {}", fromId, toId, counter);
                }
            }
            throw new RetryOperationException(String.format("Number of tries was exceeded (from user id %s to user id %s)", fromId, toId));
        } catch (RuntimeException e) {
            log.error("Operation from user id '{}' to user id '{}' was failed", fromId, toId, e);
            throw e;
        } catch (Exception e) {
            log.error("Operation from user id '{}' to user id '{}' was failed", fromId, toId, e);
            throw new RuntimeException(e);
        }
    }
}
