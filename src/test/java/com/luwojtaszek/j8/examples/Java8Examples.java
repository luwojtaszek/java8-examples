package com.luwojtaszek.j8.examples;

import com.google.common.collect.ImmutableSet;
import com.luwojtaszek.j8.examples.java8.InMemoryUserRepository;
import com.luwojtaszek.j8.examples.java8.SomeService;
import com.luwojtaszek.j8.examples.java8.User;
import com.luwojtaszek.j8.examples.java8.UserRepository;
import com.luwojtaszek.j8.examples.java8.stock.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.time.Month.APRIL;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * https://www.journaldev.com/2389/java-8-features-with-examples
 * https://twitter.com/intellijidea/status/862961379329888256
 */
@Slf4j
@SuppressWarnings("Duplicates")
public class Java8Examples {

    private static final UserRepository userRepository = new InMemoryUserRepository();

    public void example_000__interfaces() {
        // see UserRepository
    }

    @Test
    public void example_001__for() {
        final List<String> words = Lists.newArrayList("Ala", "Ma", "Kota");
        String hello = "hello";
        for (String word : words) {
            log.info("{} -> {}", hello, word);
            hello = "bonjour";
        }
    }

    @Test
    public void example_002__forEach() {
        final List<String> words = Lists.newArrayList("Ala", "Ma", "Kota");
        String hello = "hello";
        words.forEach(word -> {
            log.info("{} -> {}", hello, word);
//            hello = "modyfikacja"; // nie mozna mutowac
        });
    }

    @Test
    public void example_003__forEach_shorten() {
        final List<String> words = Lists.newArrayList("Ala", "Ma", "Kota");
        words.forEach(log::info);
    }

    @Test
    public void example_004__functional_interface() {
        final User user = new User(1L, "Lukasz", "Wojtaszek", 11);
        final SomeService<User> service = new SomeService<>(user);
        service.doLogic(u -> u.getFirstName() + " " + u.getLastName());
    }

    @Test
    public void example_005__functional_interface() {
        final SomeService<BigDecimal> service = new SomeService<>(BigDecimal.valueOf(500.1234));
        service.doLogic(source -> "Discount value: " + source.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "$");
        service.doLogic(source -> "Total Price: " + source.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "$");
    }

    @Test
    public void example_006__lambda() {
        final Runnable runnableJ7 = new Runnable() {
            @Override
            public void run() {
                log.info("My Runnable J7");
            }
        };
        final Runnable runnableJ8 = () -> log.info("My Runnable J8");

        runnableJ7.run();
        runnableJ8.run();
    }

    @Test
    public void example_007__local_date() {
        final LocalDate now = LocalDate.now();
        final Date nowLegacy = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());

        log.info("Now: {}", now);
        log.info("one year ago: {}", now.minusYears(1));
        log.info("20 days later: {}", now.plusDays(20));

        log.info("Legacy Date: {}", nowLegacy);
    }

    @Test
    public void example_008__local_datetime() {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime nowUTC = LocalDateTime.now(ZoneId.of("UTC"));
        final LocalDateTime specific = LocalDateTime.of(1986, APRIL, 8, 12, 30);
        final LocalDateTime parsed = LocalDateTime.parse("2018-02-12 13:38", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        log.info("Now: {}", now);
        log.info("Now UTC: {}", nowUTC);
        log.info("Specific: {}", specific);
        log.info("Parsed: {}", parsed);
    }

    @Test
    public void example_009__concurrent_hashmap() {
        final ConcurrentHashMap<Long, User> map = new ConcurrentHashMap<>();
        map.put(1L, new User(1L, "Szymon", "Jakis", 12));
        map.put(2L, new User(2L, "Lukasz", "Wojtaszek", 14));
        map.put(3L, new User(3L, "Piotr", "Chleb", 16));

        map.forEach((id, user) -> log.info("ID: {}, User: {}", id, user));

        final User user = map.search(2, (id, user1) -> id > 2 && user1.getFirstName().startsWith("P") ? user1 : null);
        log.info("Matching user: {}", user);
    }

    @Test
    public void example_010__optional() {
        final Function<User, String> resolveUserFullName = user -> user.getFirstName() + " " + user.getLastName();

        final String userFullName1 = userRepository.findById(1000L) // not existing id
                .map(resolveUserFullName) // mapping user -> string
                .orElse("Anonymous");
        assertEquals("Anonymous", userFullName1);

        final String userFullName2 = userRepository.findById(1L) // existing id
                .map(resolveUserFullName) // mapping user -> string
                .orElse("Anonymous");
        assertEquals("Lukasz Wojtaszek", userFullName2);
    }

    @Test
    public void example_011__stream() {
        final User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        final BigDecimal totalCartValue = user.getCart().getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final CartItem mostExpensiveItem = user.getCart().getItems().stream()
                .max(Comparator.comparing(CartItem::getTotalPrice))
                .orElseThrow(() -> new IllegalStateException("User does not have any items"));

        assertEquals(0, BigDecimal.valueOf(58.2).compareTo(totalCartValue));
        assertEquals("coca-cola", mostExpensiveItem.getId());
    }

    @Test
    public void example_012__stream_flat_map_and_reduce() {
        final BigDecimal totalIncome = userRepository.findUsers().stream()
                .map(User::getCart)
                .flatMap(cart -> cart.getItems().stream())
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(0, BigDecimal.valueOf(10560.20).compareTo(totalIncome));
    }

    @Test
    public void example_013__stream_group_by() {
        final Map<String, List<CartItem>> groupedByType = userRepository.findUsers().stream()
                .map(User::getCart)
                .flatMap(cart -> cart.getItems().stream())
                .collect(groupingBy(CartItem::getType));

        log.info("Result: {}", groupedByType);
        assertEquals(1, groupedByType.get("food").size());
        assertEquals(3, groupedByType.get("drink").size());
        assertEquals(3, groupedByType.get("instrument").size());
    }

    @Test
    public void example_014__collect_to_map() {
        final Map<String, User> users = userRepository.findUsers().stream()
                .collect(toMap(User::getFirstName, u -> u, (o, o2) -> o)); // 3rd param -> merging function is important

        log.info("Lukasz: {}", users.get("Lukasz"));
        assertEquals(2, users.keySet().size());
        assertNotNull(users.get("Lukasz"));
    }

    @Test
    public void example_015__collect_to_treeset() {
        final Set<String> uniqueSortedUserNames = userRepository.findUsers().stream()
                .map(User::getFirstName)
                .sorted(Comparator.naturalOrder())
                .collect(toCollection(TreeSet::new));

        assertEquals(2, uniqueSortedUserNames.size());
        assertTrue(uniqueSortedUserNames.containsAll(ImmutableSet.of("Jan", "Lukasz")));
    }
}
