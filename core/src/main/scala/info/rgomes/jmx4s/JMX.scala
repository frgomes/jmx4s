package info.rgomes.jmx4s

import java.lang.management.ManagementFactory
import javax.management.ObjectName
import javax.management.modelmbean.{ModelMBeanInfoSupport, ModelMBeanOperationInfo, RequiredModelMBean}


trait JMX {
  self =>
  Registrar.register(self)
}

object Registrar {
  import scala.reflect.runtime.{universe => ru}

  val mbs = ManagementFactory.getPlatformMBeanServer

  def register(jmxe: JMX) = {
    val rm = ru.runtimeMirror(getClass.getClassLoader)
    val mType = rm.classSymbol(jmxe.getClass).selfType
    var ops = List[ModelMBeanOperationInfo]()
    mType.declarations.foreach(symbol => {
      symbol.annotations.find(a => a.tpe == ru.typeOf[Manageable]) match {
        case Some(_) => {
          import language.reflectiveCalls
          val cmx = rm.asInstanceOf[{ def methodToJava(sym: scala.reflect.internal.Symbols#MethodSymbol): java.lang.reflect.Method }]
          val jm = cmx.methodToJava(symbol.asMethod.asInstanceOf[scala.reflect.internal.Symbols#MethodSymbol])
          ops = ops :+ new ModelMBeanOperationInfo(jm.toString, jm)
        }
        case None =>
      }
    })
    val mbeansOps = ops.toArray
    val name = jmxe.getClass.getName
    val modelMBeanInfoSupport =
      new ModelMBeanInfoSupport(
        name, jmxe + " Model MBean", null, null, mbeansOps, null)
    val requiredModelMBean =
      new RequiredModelMBean(
        modelMBeanInfoSupport)
    requiredModelMBean.setManagedResource(jmxe, "ObjectReference")
    val mBeanServer = ManagementFactory.getPlatformMBeanServer()
    val pkg = jmxe.getClass.getPackage.getName
    mBeanServer.registerMBean(requiredModelMBean, new ObjectName(s"${pkg}:type="+jmxe))
  }
}