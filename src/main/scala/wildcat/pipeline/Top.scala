package wildcat.pipeline

import chisel3._
import wildcat.Util
import chisel.lib.uart._
import wildcat.pipeline.peripherals._

class Top (file: String, freq: Int, baud: Int) extends Module{
  val io = IO(new Bundle {
    val led = Output(UInt(16.W))
    val tx = Output(UInt(1.W))
    val rx = Input(UInt(1.W))
    val sw = Input(UInt(16.W))
    val btn = Input(UInt(4.W))
    val PS2_CLK = Input(UInt(1.W))
    val PS2_DATA = Input(UInt(1.W))
    val vga = new VGA()
  })

  val syncReset = RegNext(RegNext(RegNext(reset)))

  val delRx = RegNext(RegNext(RegNext(io.rx)))
  val delBtn = RegNext(RegNext(RegNext(io.btn)))
  val delSw = RegNext(RegNext(RegNext(io.sw)))
  val delPS2_CLK = RegNext(RegNext(RegNext(io.PS2_CLK)))
  val delPS2_DATA = RegNext(RegNext(RegNext(io.PS2_DATA)))
  val delVga = RegNext(RegNext(RegNext(io.vga)))

  val wildcat = withReset(syncReset){Module(new WildcatTop(file, 75000000, 115200))}

  io.tx := RegNext(RegNext(RegNext(wildcat.io.tx)))
  io.led := RegNext(RegNext(RegNext(wildcat.io.led)))

  wildcat.io.rx := delRx
  wildcat.io.btn := delBtn
  wildcat.io.sw := delSw
  wildcat.io.PS2_CLK := delPS2_CLK
  wildcat.io.PS2_DATA := delPS2_DATA
  wildcat.io.vga := delVga
}

object Top extends App {
  emitVerilog(new Top(args(0), 75000000, 115200), Array("--target-dir", "generated"))
}