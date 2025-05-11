package wildcat.pipeline

import chisel3._
import wildcat.Util
import chisel.lib.uart._
import wildcat.pipeline.peripherals._

class Top (file: String) extends Module{
  val io = IO(new TopIO())

  val syncReset = RegNext(RegNext(RegNext(reset)))
  val delBtn = RegNext(RegNext(RegNext(io.btn)))
  val delSw = RegNext(RegNext(RegNext(io.sw)))

  val wildcat = withReset(syncReset){Module(new WildcatTop(file))}

  io <> wildcat.io
  wildcat.io.btn := delBtn
  wildcat.io.sw := delSw
}

object Top extends App {
  emitVerilog(new Top(args(0)), Array("--target-dir", "generated"))
}