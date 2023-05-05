dependencies {
    implementation(project(":eladmin-common"))
    implementation(project(":eladmin-logging"))
    implementation(project(":eladmin-tools"))
    implementation("org.quartz-scheduler:quartz")
    implementation("com.github.oshi:oshi-core:6.4.0")
    runtimeOnly(project(":eladmin-generator"))
}
