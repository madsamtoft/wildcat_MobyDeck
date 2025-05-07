// Code inspired from https://github.com/Digilent/Basys-3-Keyboard/blob/master/src/hdl/PS2Receiver.v

package wildcat.pipeline.peripherals

import chisel3._
import chisel3.util._

class PS2Receiver extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
    val kclk = Input(Bool())
    val kdata = Input(Bool())
    val keycodeout = Output(UInt(16.W))
  })
  addResource("/Debouncer.v")
  addResource("/PS2Receiver.v")
}

/*
class PS2Receiver extends Module {
  val io = IO(new Bundle{
    val ps2Clock = Input(Bool())
    val ps2Data = Input(Bool())
    val ps2Out = Output(UInt(16.W))
  })

  //Clock
  val ps2Falling = RegNext(io.ps2Clock) && !io.ps2Clock

  //Enumeration of input states
  object PS2State extends ChiselEnum {
    val start, data0, data1, data2, data3, data4, data5, data6, data7, parity, stop = Value
  }
  import PS2State._

  val state = RegInit(start)
  val preOut = RegInit(0.U(8.W))
  io.ps2Out := 0.U

  when(ps2Falling) {
    switch(state) {
      is(start) {
        when(io.ps2Data === 0.B) {
          state := data0
        }
      }
      is(data0, data1, data2, data3, data4, data5, data6, data7) {
        preOut := Cat(io.ps2Data, preOut(7, 1)) // fix the bit order too!
        state := PS2State((state.asUInt + 1.U).asUInt)
      }
      is(stop) {
        when(io.ps2Data === 1.B){
          io.ps2Out := preOut
        }
        state := start
      }
    }
  }
}
 */