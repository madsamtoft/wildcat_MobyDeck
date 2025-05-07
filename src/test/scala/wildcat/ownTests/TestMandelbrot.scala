package wildcat.ownTests

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import wildcat.pipeline._

class TestMandelbrot extends AnyFlatSpec with ChiselScalatestTester {
  "_Mandelbrot test" should "pass" in {
    test(new WildcatTop("a.out")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)
      dut.clock.step(1000000)
    }
  }
}
