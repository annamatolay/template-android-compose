package dev.anmatolay.template.compose.core

import androidx.test.ext.junit.rules.ActivityScenarioRule
import dev.anmatolay.template.compose.ui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
open class InstrumentedTest(
    activityScenarioRule: ActivityScenarioRule<*> = ActivityScenarioRule(MainActivity::class.java),
    val modules: MutableList<Module> = mutableListOf(),
//    val mockEndpointList: MutableList<MockServer.MockEndpoint> = mutableListOf()
) : KoinTest {

    @get:Rule
    val activityRule = activityScenarioRule

    @get:Rule
    val koinTestRule = KoinTestRule.create()

//    private lateinit var server: MockServer

    @Before
    fun setUp() {
//        server = MockServer(mockEndpointList)
//        server.start()
    }

    @After
    fun tearDown() {
//        server.stop()
    }
}
