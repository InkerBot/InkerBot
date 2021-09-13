package com.eloli.inkerbot.core.dependency

import com.eloli.inkerbot.api.Frame
import com.eloli.inkerbot.core.setting.InkSetting
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.AbstractRepositoryListener
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositoryEvent
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.impl.DefaultServiceLocator
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResolutionException
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transfer.AbstractTransferListener
import org.eclipse.aether.transfer.TransferEvent
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DependencyResolver @Inject constructor(frame: Frame, setting: InkSetting) {
    private val repositorySystem: RepositorySystem
    private val repositorySystemSession: DefaultRepositorySystemSession
    private val remoteRepositories: MutableList<RemoteRepository> = LinkedList()
    @Throws(ArtifactResolutionException::class)
    fun getDependencyFile(packageName: String): File {
        val splits = packageName.split(":").toTypedArray()
        val group = splits[0]
        val name = splits[1]
        val version = splits[2]
        val artifact: Artifact = DefaultArtifact(group, name, "jar", version)
        val artifactRequest = ArtifactRequest()
        artifactRequest.artifact = artifact
        artifactRequest.repositories = remoteRepositories
        val artifactResult: ArtifactResult
        artifactResult = repositorySystem.resolveArtifact(repositorySystemSession, artifactRequest)
        return if (artifactResult.isResolved) {
            artifactResult.artifact.file
        } else {
            throw ArtifactResolutionException(listOf(artifactResult))
        }
    }

    class ConsoleRepositoryListener : AbstractRepositoryListener() {
        override fun artifactDeployed(event: RepositoryEvent) {
            //
        }

        override fun artifactDeploying(event: RepositoryEvent) {
            //
        }

        override fun artifactDescriptorInvalid(event: RepositoryEvent) {
            logger.warn("Invalid artifact descriptor for {}: {}.", event.artifact, event.exception.message)
        }

        override fun artifactDescriptorMissing(event: RepositoryEvent) {
            logger.warn("Missing artifact descriptor for {}.", event.artifact)
        }

        override fun artifactInstalled(event: RepositoryEvent) {
            //
        }

        override fun artifactInstalling(event: RepositoryEvent) {
            //
        }

        override fun artifactResolved(event: RepositoryEvent) {
            //
        }

        override fun artifactDownloading(event: RepositoryEvent) {
            //
        }

        override fun artifactDownloaded(event: RepositoryEvent) {
            //
        }

        override fun artifactResolving(event: RepositoryEvent) {
            //
        }

        override fun metadataDeployed(event: RepositoryEvent) {
            //
        }

        override fun metadataDeploying(event: RepositoryEvent) {
            //
        }

        override fun metadataInstalled(event: RepositoryEvent) {
            //
        }

        override fun metadataInstalling(event: RepositoryEvent) {
            //
        }

        override fun metadataInvalid(event: RepositoryEvent) {
            logger.warn("Invalid metadata: {}.", event.metadata)
        }

        override fun metadataResolved(event: RepositoryEvent) {
            //
        }

        override fun metadataResolving(event: RepositoryEvent) {
            //
        }
    }

    class ConsoleTransferListener : AbstractTransferListener() {
        private var time = System.currentTimeMillis()
        override fun transferInitiated(event: TransferEvent) {
            //
        }

        override fun transferProgressed(event: TransferEvent) {
            val now = System.currentTimeMillis()
            if (time + 1000 < now) {
                time = now
                logger.info(
                    "Downloading: {} {} ({}%).",
                    event.resource.repositoryUrl,
                    event.resource.resourceName, (event.transferredBytes * 100 / event.resource.contentLength).toInt()
                )
            }
        }

        override fun transferSucceeded(event: TransferEvent) {
            //
        }

        override fun transferFailed(event: TransferEvent) {
            //
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger("dependency-resolver")
    }

    init {
        val dependencyPath = frame.self.dataPath.resolve("dependency")
        Files.createDirectories(dependencyPath)

        // FIXME: Deprecated class DefaultServiceLocator
        val locator = MavenRepositorySystemUtils.newServiceLocator()
        locator.addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
        locator.addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)
        locator.setErrorHandler(object : DefaultServiceLocator.ErrorHandler() {
            override fun serviceCreationFailed(type: Class<*>?, impl: Class<*>?, exception: Throwable) {
                logger.warn(
                    String.format("Service creation failed for %s with implementation %s", type, impl),
                    Exception(exception)
                )
            }
        })
        repositorySystem = locator.getService(RepositorySystem::class.java)
        repositorySystemSession = MavenRepositorySystemUtils.newSession()
        repositorySystemSession.transferListener = ConsoleTransferListener()
        repositorySystemSession.repositoryListener = ConsoleRepositoryListener()
        val localRepo = LocalRepository(dependencyPath.toFile())
        repositorySystemSession.localRepositoryManager =
            repositorySystem.newLocalRepositoryManager(repositorySystemSession, localRepo)
        repositorySystemSession.setSystemProperties(System.getProperties())
        repositorySystemSession.setConfigProperties(System.getProperties())
        repositorySystemSession.setSystemProperty(
            "os.detected.name",
            System.getProperty("os.name", "unknown").lowercase(Locale.getDefault())
        )
        repositorySystemSession.setSystemProperty(
            "os.detected.arch",
            System.getProperty("os.arch", "unknown").replace("amd64", "x86_64")
        )
        for (repo in setting.mavenRepo) {
            remoteRepositories.add(RemoteRepository.Builder(null, "default", repo).build())
        }
    }
}