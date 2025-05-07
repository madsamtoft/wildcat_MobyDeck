package wildcat.ownTests

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import wildcat.pipeline._

class TestVgaSimple extends AnyFlatSpec with ChiselScalatestTester {
  "_VgaSimple test" should "pass" in {
    test(new WildcatTop("../SoC_project/out/vga_simple.out")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)
      dut.io.sw.poke("b001000".U)
      dut.clock.step(100)
      dut.io.sw.poke("b110000".U)
      dut.clock.step(10000)
    }
  }
}
