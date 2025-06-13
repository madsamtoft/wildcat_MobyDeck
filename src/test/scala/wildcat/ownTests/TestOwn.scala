package wildcat.ownTests

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import wildcat.pipeline._

class TestOwn extends AnyFlatSpec with ChiselScalatestTester {
  "_Assembly test" should "pass" in {
    test(new WildcatTop("a.out", 100000000, 115200)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)
      dut.clock.step(100)
      dut.io.sw.poke(0xDEAD.U)
      dut.clock.step(2000)
    }
  }
}
