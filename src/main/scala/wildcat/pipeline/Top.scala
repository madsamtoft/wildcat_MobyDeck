package wildcat.pipeline

import chisel3._
import wildcat.Util
import chisel.lib.uart._
import wildcat.pipeline.peripherals._

class Top (file: String) extends Module{
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
  val delBtn = RegNext(RegNext(RegNext(io.btn)))
  val delSw = RegNext(RegNext(RegNext(io.sw)))

  val wildcat = withReset(syncReset){Module(new WildcatTop(file))}

  io <> wildcat.io
  wildcat.io.btn := delBtn
  wildcat.io.sw := delSw
}

object Top extends App {
  emitVerilog(new WildcatTop(args(0)), Array("--target-dir", "generated"))
}