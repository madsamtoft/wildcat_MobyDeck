package wildcat.pipeline.peripherals

import chisel3._
import chisel3.util._

class clk_wiz_0 extends BlackBox {
  val io = IO(new Bundle {
    val clock_in = Input(Clock())
    val clock_out = Output (Clock())
  })
}
