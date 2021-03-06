package com.atlassian.performance.tools.jiraperformancetests.api

import com.atlassian.performance.tools.jiraperformancetests.AcceptanceCategory
import com.atlassian.performance.tools.jiraperformancetests.MavenProcess
import com.atlassian.performance.tools.jiraperformancetests.SystemProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.experimental.categories.Category
import org.zeroturnaround.exec.ProcessExecutor
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

class VendorJourneyTest {

    @Test
    @Category(AcceptanceCategory::class)
    fun shouldRunRefApp() {
        val jptVersion: String = SystemProperty("jpt.version").dereference()
        val mavenProcess = MavenProcess(
            arguments = listOf("install", "-Djpt.version=$jptVersion"),
            processExecutor = ProcessExecutor()
                .directory(Paths.get("examples", "ref-app").toFile())
                .timeout(55, TimeUnit.MINUTES)
        )

        val result = mavenProcess.run()

        val lastFewLinesOfOutput = result.output.lines.takeLast(12).joinToString(separator = "\n")
        assertThat(lastFewLinesOfOutput)
            .`as`("last few lines of output")
            .contains("BUILD SUCCESS")
    }
}
