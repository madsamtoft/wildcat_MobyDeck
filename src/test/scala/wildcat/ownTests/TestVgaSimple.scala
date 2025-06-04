package wildcat.ownTests

import chisel3._
import chiseltest._
import chiseltest.simulator.VerilatorBackendAnnotation
import chiseltest.simulator.VerilatorFlags
import org.scalatest.flatspec.AnyFlatSpec
import wildcat.pipeline._

class TestVgaSimple extends AnyFlatSpec with ChiselScalatestTester {
  "_VgaSimple test" should "pass" in {
    test(new WildcatTop("../SoC_project/out/vga_simple.out", 100000000, 115200)).withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation, VerilatorFlags(Seq("-CFLAGS", "-std=c++14")))) { dut =>
      dut.clock.setTimeout(0)
      dut.clock.step(100)
      dut.io.sw.poke("b001000".U)
      dut.clock.step(6000)

//      dut.io.sw.poke("b000001".U)
      dut.clock.step(100000)
    }
  }
  "_VgaSimple test2" should "pass" in {
    test(new WildcatTop("../SoC_project/out/vga_simple2.out", 100000000, 115200)).withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation, VerilatorFlags(Seq("-CFLAGS", "-std=c++14")))) { dut =>
      dut.clock.setTimeout(0)
      dut.clock.step(100)
      dut.io.sw.poke("b001000".U)
      dut.clock.step(6000)

//      dut.io.sw.poke("b000001".U)
      dut.clock.step(100000)
    }
  }
}