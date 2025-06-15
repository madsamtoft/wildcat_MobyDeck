package wildcat.ownTests

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import wildcat.pipeline._

class TestTimer extends AnyFlatSpec with ChiselScalatestTester {
  "_Timer test" should "pass" in {
    test(new WildcatTop("../SoC_project/out/timer.out", 50000000, 115200)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)
      dut.clock.step(2000)
    }
  }
}
