//package easyconfig
//
//import shapeless.{::, HList, HNil, LabelledGeneric, Lazy, Witness}
//import shapeless.labelled.FieldType
//import shapeless.syntax.singleton._
//import shapeless.labelled._
//
//trait EnvReader2[A] {
//  def readEnv: A
//}
//
//object EnvReader2 {
//
//  def fieldNameToEnvVar(name: String): String = name.flatMap( c => if(c.isUpper) List('_', c) else List(c)).mkString.toUpperCase
//  def fieldNameToParam(name: String): String = "--" + name.flatMap( c => if(c.isUpper) List('-', c) else List(c)).mkString.toLowerCase
//
//  def getEnv[A](envVal: String)(implicit parser: Parser[A]): Option[A] = {
//    val envValValue = sys.env.get(envVal)
//
//    envValValue.map(parser.parse)
//  }
//
//  def apply[A](implicit envReader: EnvReader2[A]): EnvReader2[A] = envReader
//
//  implicit val hnilEnvReader: EnvReader2[HNil] = new EnvReader2[HNil] {
//
//    def readEnv = HNil
//  }
//
//  implicit def fieldTypeEnvReader[K <: Symbol, V](implicit witness: Witness.Aux[K], parser: Parser[V]): EnvReader2[FieldType[K, V]] =
//    new EnvReader2[FieldType[K, V]] {
//
//      def readEnv: FieldType[K, V] = field[K](getEnv(fieldNameToEnvVar(witness.value.name)).get) // TODO unsafe get
//    }
//
//
//  implicit def hlistEnvReader[H, T <: HList](implicit hEnvReader: Lazy[EnvReader2[H]], tEnvReader: EnvReader2[T]): EnvReader2[H :: T] =
//    new EnvReader2[H :: T] {
//
//      def readEnv: H :: T = hEnvReader.value.readEnv :: tEnvReader.readEnv
//    }
//
//  implicit def genericEnvReader[A, R](implicit generic: LabelledGeneric.Aux[A, R], envReader: Lazy[EnvReader2[R]]): EnvReader2[A] = new EnvReader2[A] {
//
//    def readEnv: A = generic.from(envReader.value.readEnv)
//  }
//
//}
