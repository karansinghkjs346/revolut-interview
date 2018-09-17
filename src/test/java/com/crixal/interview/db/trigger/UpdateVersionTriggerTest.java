package com.crixal.interview.db.trigger;

import com.crixal.interview.db.repository.RepositoryFactory;
import com.crixal.interview.db.repository.UserRepository;
import com.crixal.interview.dto.UserDTO;
import common.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateVersionTriggerTest extends BaseTest {
    @Before
    public void before() {
        clearTestData();
    }

    @Test
    public void checkVersionTrigger() throws Exception {
        saveUser("u1", new BigDecimal(10));
        saveUser("u2", new BigDecimal(0));

        try (UserRepository repository = injector.getInstance(RepositoryFactory.class).getUserRepository()) {
            Map<String, Long> name2Version = repository
                    .getUsers()
                    .stream()
                    .collect(Collectors.toMap(UserDTO::getName, UserDTO::getVersion));

            Assert.assertEquals("Wrong number of saved users", 2, name2Version.size());
            Assert.assertTrue("User version after insert should be greater then 0", name2Version.get("u1") > 0);
            Assert.assertTrue("User version after insert should be greater then 0", name2Version.get("u2") > 0);
            Assert.assertEquals("Differens between versions should be 1", 1,
                    name2Version.get("u2") - name2Version.get("u1"));
        }
    }
}