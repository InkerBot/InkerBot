package bot.inker.core.test

import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InjectTestExtension::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class InjectTest 