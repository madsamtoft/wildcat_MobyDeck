package wildcat.pipeline

import chisel3._
import wildcat.Util
import chisel.lib.uart._
import wildcat.pipeline.peripherals._

class Top (file: String, freq: Int, baud: Int) extends Module{
  val io = IO(new TopIO())
  val wiz = Module(new clk_wiz_0)
  wiz.io.clock_board := clock

  withClock(wiz.io.clock) {
    val syncReset = RegNext(RegNext(RegNext(reset)))
    val wildcat = withReset(syncReset){Module(new WildcatTop(file, freq, baud))}

    io.led := wildcat.io.led
    io.tx := wildcat.io.tx
    wildcat.io.rx := RegNext(RegNext(io.rx))
    wildcat.io.sw := RegNext(RegNext(io.sw))
    wildcat.io.btn := RegNext(RegNext(io.btn))
    wildcat.io.PS2_CLK := RegNext(RegNext(io.PS2_CLK))
    wildcat.io.PS2_DATA := RegNext(RegNext(io.PS2_DATA))
    io.vga <> wildcat.io.vga
  }
}

object Top extends App {
  emitVerilog(new Top(args(0), 75000000, 115200), Array("--target-dir", "generated"))
}
