package com.healthly.symptomchecker.repository.impl;

import com.healthly.symptomchecker.entity.User;
import com.healthly.symptomchecker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Optional;

import static com.healthly.symptomchecker.data.SecondaryPartitionKeys.USER_EMAIL_INDEX;

@Repository
@AllArgsConstructor
public class UserRepositoryDynamoImpl implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryDynamoImpl.class);
    private final DynamoDbTable<User> userTable;

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user);
        userTable.putItem(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        if (!StringUtils.hasText(email)) {
            log.warn("Attempt to search user by email but no value has been provided");
            return Optional.empty();
        }

        log.debug("Searching for user by email: {}", email);

        QueryConditional queryConditional =
                QueryConditional.keyEqualTo(k -> k.partitionValue(email));

        final Optional<Page<User>> userPages = userTable
                .index(USER_EMAIL_INDEX)
                .query(req -> req.queryConditional(queryConditional))
                .stream()
                .findFirst();

        if (userPages.isEmpty()) {
            log.debug("No user found for email: {}", email);
            return Optional.empty();
        }

        final Page<User> userPage = userPages.get();
        if (userPage.items().size() > 1) {
            log.warn("Found more than one user with email: {}", email);
            throw new IllegalStateException(
                    "Found more than one user with email: %s".formatted(email));
        }

        return userPage.items().stream().findFirst();
    }
}
