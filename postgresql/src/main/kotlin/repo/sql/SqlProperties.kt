open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/swipe",
    val user: String = "postgres",
    val password: String = "swipe-pass",
    val schema: String = "swipe",
    // Delete tables at startup - needed for testing
    val dropDatabase: Boolean = false,
)