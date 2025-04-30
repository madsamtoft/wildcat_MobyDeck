package wildcat

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import wildcat.pipeline.WildcatTop

class CompileTest(file: String) extends AnyFlatSpec with ChiselScalatestTester {
  "Compile Test" should "pass" in {
    test(new WildcatTop(file))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        dut.io.btn.poke(0.U)
        dut.io.sw.poke(0.U)
        dut.io.PS2_CLK.poke(0.U)
        dut.io.PS2_DATA.poke(0.U)

        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000000001".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000000011".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000000111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000001111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000011111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000001111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000011111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000001111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000011111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000111111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0001111111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0011111111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0111111111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b1111111111111111".U)
        dut.clock.step(10)
        dut.io.sw.poke("0b0000000000000000".U)
      }
  }
}
