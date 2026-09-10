import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
val minimumBranchCoverage = 80

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover)
}

subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            buildUponDefaultConfig = true
            allRules = false
            config.setFrom(rootProject.files("config/detekt/detekt.yml"))
            baseline = file("detekt-baseline.xml").takeIf { it.exists() }
        }
        tasks.withType<Detekt>().configureEach {
            reports {
                html.required.set(true)
                xml.required.set(true)
                txt.required.set(false)
                sarif.required.set(false)
            }
        }
        // AGP wires its own JVM target into the Detekt tasks during evaluation, so pin ours after.
        afterEvaluate {
            tasks.withType<Detekt>().configureEach {
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
            tasks.withType<DetektCreateBaselineTask>().configureEach {
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }
}

dependencies {
    kover(project(":app"))
    kover(project(":designsystem"))
    kover(project(":core:domain"))
    kover(project(":core:data"))
    kover(project(":core:llm"))
    kover(project(":feature:welcome"))
    kover(project(":feature:llmsetup"))
    kover(project(":feature:home"))
    kover(project(":feature:quiz"))
    kover(project(":feature:results"))
}

kover {
    reports {
        total {
            html { onCheck = false }
            xml { onCheck = false }

            log {
                onCheck = false
                header = "Branch coverage"
                coverageUnits = kotlinx.kover.gradle.plugin.dsl.CoverageUnit.BRANCH
            }

            verify {
                rule("Minimum branch coverage") {
                    bound {
                        minValue = minimumBranchCoverage
                        coverageUnits = kotlinx.kover.gradle.plugin.dsl.CoverageUnit.BRANCH
                    }
                }
            }
        }

        filters {
            excludes {
                // Compose UI, generated artifacts, DI wiring and Android entry points hold no
                // testable logic; they are covered by previews and instrumented tests instead.
                classes(
                    "*.BuildConfig",
                    "*.R",
                    "*.R$*",
                    "*ComposableSingletons*",
                    "*ScreenKt",
                    "*.MainActivity",
                    "*.QuiziaApplication",
                    // Wraps the ML Kit GenAI runtime, which only exists on a device.
                    "*.MlKitOnDeviceModelSession",
                )
                packages(
                    "com.vitorfg8.quizia.designsystem",
                    "com.vitorfg8.quizia.designsystem.components",
                    "com.vitorfg8.quizia.di",
                    "com.vitorfg8.quizia.core.data.di",
                    "com.vitorfg8.quizia.core.llm.di",
                    "com.vitorfg8.quizia.core.llm.network.dto",
                    "com.vitorfg8.quizia.feature.home.di",
                    "com.vitorfg8.quizia.feature.llmsetup.di",
                    "com.vitorfg8.quizia.feature.quiz.di",
                    "com.vitorfg8.quizia.feature.results.di",
                )
                annotatedBy("androidx.compose.runtime.Composable")
            }
        }
    }
}
