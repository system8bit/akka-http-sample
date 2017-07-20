import org.apache.commons.daemon.{Daemon, DaemonContext}
import org.slf4j.LoggerFactory

trait ApplicationLifecycle {
  def start(): Unit

  def stop(): Unit
}

class ApplicationDaemon extends Daemon {
  val app: ApplicationLifecycle = new Application

  override def init(context: DaemonContext): Unit = {}

  override def stop(): Unit = app.stop()

  override def destroy(): Unit = app.stop()

  override def start(): Unit = app.start()
}

object Main extends App {
  val logger = LoggerFactory.getLogger("Main")
  val app = new ApplicationDaemon
  app.start()
  logger.info("Press RETURN to stop...")
  scala.io.StdIn.readLine()
  app.stop()
}
