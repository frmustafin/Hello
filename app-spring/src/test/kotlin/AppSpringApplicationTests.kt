package fr.mustafin.demo

import RepoProfileSQL
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AppSpringApplicationTests {

	@MockkBean
	private lateinit var repo: RepoProfileSQL

	@Test
	fun contextLoads() {
	}

}
