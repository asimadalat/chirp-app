import androidx.room.gradle.RoomExtension
import com.asimorphic.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with (receiver = target) {
            with (receiver = pluginManager) {
                apply("com.google.devtools.ksp")
                apply("androidx.room")
            }

            extensions.configure<RoomExtension> {
                schemaDirectory(path = "$projectDir/schemas")
            }

            dependencies {
                "commonMainApi"(dependency = libs.findLibrary("androidx-room-runtime").get())
                "commonMainApi"(dependency = libs.findLibrary("sqlite-bundled").get())
                "kspAndroid"(dependency = libs.findLibrary("androidx-room-compiler").get())
                "kspIosSimulatorArm64"(dependency = libs.findLibrary("androidx-room-compiler").get())
                "kspIosArm64"(dependency = libs.findLibrary("androidx-room-compiler").get())
                "kspIosX64"(dependency = libs.findLibrary("androidx-room-compiler").get())
                //"kspJvm"(dependency = libs.findLibrary("androidx-room-compiler").get())
            }
        }
    }
}