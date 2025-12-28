package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.Users;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(Users.builder().username("pne45sa5w").password("prince").build()),
                Arguments.of(Users.builder().username("useas456rs2").password("123").build())
        );
    }
}
