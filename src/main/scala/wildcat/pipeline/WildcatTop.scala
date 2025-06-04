package wildcat.pipeline

import chisel3._
import wildcat.Util
import chisel.lib.uart._
import wildcat.pipeline.peripherals._

/*
 * This file is part of the RISC-V processor Wildcat.
 *
 * This is the top-level for a three stage pipeline.
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */
class WildcatTop(file: String, freq: Int, baud: Int) extends Module {

  val io = IO(new TopIO())


  val (memory, start) = Util.getCode(file)

  // Here switch between different designs
  val cpu = Module(new ThreeCats())
  // val cpu = Module(new WildFour())
  // val cpu = Module(new StandardFive())
  val dmem = Module(new ScratchPadMem(memory))
  cpu.io.dmem <> dmem.io
  val imem = Module(new InstructionROM(memory))
  imem.io.address := cpu.io.imem.address
  cpu.io.imem.data := imem.io.data
  cpu.io.imem.stall := imem.io.stall
  // TODO: stalling


  //Testing ps2 module :)
  val ps2 = Module(new PS2Receiver)
  ps2.io.clk := clock
  ps2.io.kdata := io.PS2_DATA
  ps2.io.kclk := io.PS2_CLK

  /* !!! This MIGHT be needed later on !!!
  val ps2_keycode = RegInit(0.U(8.W))
  val ps2_press = RegInit(false.B)
  ps2_keycode := ps2.io.keycodeout(7,0)


  when(ps2_keycode === 0xF0.U) {
    ps2_press := false.B
    ps2_keycode := 0x00.U
  } .elsewhen(ps2_keycode =/= 0x00.U) {
    ps2_press := true.B
  }
  */

  // Here IO stuff
  // IO is mapped ot 0xf000_0000
  // use lower bits to select IOs

  // UART:
  // 0xf000_0000 status:
  // bit 0 TX ready (TDE)
  // bit 1 RX data available (RDF)
  // 0xf000_0004 send and receive register

  // LEDs:      0xf001_0000

  // Switches:  0xf002_0000

  // Buttons:   0xf003_0000

  // PS2 data:  0xf004_0000

  //VGA:        0xf010_0000

  val tx = Module(new BufferedTx(freq, baud))
  val rx = Module(new Rx(freq, baud))
  io.tx := tx.io.txd
  rx.io.rxd := io.rx

  tx.io.channel.bits := cpu.io.dmem.wrData(7, 0)
  tx.io.channel.valid := false.B
  rx.io.channel.ready := false.B

  val uartStatusReg = RegNext(rx.io.channel.valid ## tx.io.channel.ready)
  val memAddressReg = RegNext(cpu.io.dmem.rdAddress)
  val switchReg = RegNext(io.sw)
  val buttonReg = RegNext(io.btn)
  when (memAddressReg(31, 28) === 0xf.U) {  // MM-input
    when (memAddressReg(27,16) === 0.U) {   // Uart
      when (memAddressReg(3, 0) === 0.U) {
        cpu.io.dmem.rdData := uartStatusReg
      } .elsewhen(memAddressReg(3, 0) === 4.U) {
        cpu.io.dmem.rdData := rx.io.channel.bits
        rx.io.channel.ready := cpu.io.dmem.rdEnable
      }
    } .elsewhen(memAddressReg(27,16) === 2.U) { // Switches
      cpu.io.dmem.rdData := switchReg
    } .elsewhen(memAddressReg(27,16) === 3.U) { // Buttons
      cpu.io.dmem.rdData := buttonReg
    } .elsewhen(memAddressReg(27,16) === 4.U) { // ps2Data
      //cpu.io.dmem.rdData := ps2_keycode //## ps2_press
      cpu.io.dmem.rdData := ps2.io.keycodeout(7,0)
    }
  }

  // Video controller
  val DOWNSCALE_4x = false // true is 160x120, false is 320x240
  val video = Module(new VideoController(DOWNSCALE_4x, freq))
  val vgaDataReg = RegInit(0.U(32.W))
  val vgaAddressReg = RegInit(0.U(32.W))
  val vgaWriteReg = Reg(Bool())
  val anyWriteEnable = cpu.io.dmem.wrEnable(0) || cpu.io.dmem.wrEnable(1) || cpu.io.dmem.wrEnable(2) || cpu.io.dmem.wrEnable(3)
  vgaWriteReg := false.B
  video.io.data := vgaDataReg
  video.io.address := vgaAddressReg
  video.io.write := vgaWriteReg

  when ((cpu.io.dmem.wrAddress(31,20) === "xf01".U) && anyWriteEnable) { // VGA
    vgaDataReg := cpu.io.dmem.wrData
    vgaAddressReg := cpu.io.dmem.wrAddress
    vgaWriteReg := true.B
  }

  //
  val ledReg = RegInit(0.U(16.W))

  when ((cpu.io.dmem.wrAddress(31, 28) === 0xf.U) && cpu.io.dmem.wrEnable(0)) {
    when (cpu.io.dmem.wrAddress(27,16) === 0.U && cpu.io.dmem.wrAddress(3, 0) === 4.U) {
      printf(" %c %d\n", cpu.io.dmem.wrData(7, 0), cpu.io.dmem.wrData(7, 0))
      tx.io.channel.valid := true.B
    } .elsewhen (cpu.io.dmem.wrAddress(27,16) === 1.U) { // LED
      ledReg := cpu.io.dmem.wrData(15, 0)
    }
    dmem.io.wrEnable := VecInit(Seq.fill(4)(false.B))
  }

  io.led := RegNext(ledReg)
  io.vga := video.io.vga
}

object WildcatTop extends App {
  emitVerilog(new WildcatTop(args(0), 100000000, 115200), Array("--target-dir", "generated"))
}