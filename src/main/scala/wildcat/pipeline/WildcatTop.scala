package wildcat.pipeline

import chisel3._
import wildcat.Util

import chisel.lib.uart._

/*
 * This file is part of the RISC-V processor Wildcat.
 *
 * This is the top-level for a three stage pipeline.
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */
class WildcatTop(file: String) extends Module {

  val io = IO(new Bundle {
    val led = Output(UInt(16.W))
    val tx = Output(UInt(1.W))
    val rx = Input(UInt(1.W))
    val sw = Input(UInt(16.W))
    val btn = Input(UInt(4.W))
  })

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


  // Here IO stuff
  // IO is mapped ot 0xf000_0000
  // use lower bits to select IOs

  // UART:
  // 0xf000_0000 status:
  // bit 0 TX ready (TDE)
  // bit 1 RX data available (RDF)
  // 0xf000_0004 send and receive register

  // LEDs:
  // 0xf001_0000

  // Switches:
  // 0xf002_0000

  // Buttons:
  // 0xf003_0000

  val tx = Module(new BufferedTx(100000000, 115200))
  val rx = Module(new Rx(100000000, 115200))
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
    when (memAddressReg(19,16) === 0.U) {   // Uart
      when (memAddressReg(3, 0) === 0.U) {
        cpu.io.dmem.rdData := uartStatusReg
      } .elsewhen(memAddressReg(3, 0) === 4.U) {
        cpu.io.dmem.rdData := rx.io.channel.bits
        rx.io.channel.ready := cpu.io.dmem.rdEnable
      }
    } .elsewhen(memAddressReg(19,16) === 2.U) { // Switches
      cpu.io.dmem.rdData := switchReg
    } .elsewhen(memAddressReg(19,16) === 3.U) { // Buttons
      cpu.io.dmem.rdData := buttonReg
    }
  }

  val ledReg = RegInit(0.U(16.W))
  when ((cpu.io.dmem.wrAddress(31, 28) === 0xf.U) && cpu.io.dmem.wrEnable(0)) {
    when (cpu.io.dmem.wrAddress(19,16) === 0.U && cpu.io.dmem.wrAddress(3, 0) === 4.U) {
      printf(" %c %d\n", cpu.io.dmem.wrData(7, 0), cpu.io.dmem.wrData(7, 0))
      tx.io.channel.valid := true.B
    } .elsewhen (cpu.io.dmem.wrAddress(19,16) === 1.U) {
      ledReg := cpu.io.dmem.wrData(15, 0)
    }
    dmem.io.wrEnable := VecInit(Seq.fill(4)(false.B))
  }

  io.led := RegNext(ledReg)
}

object WildcatTop extends App {
  emitVerilog(new WildcatTop(args(0)), Array("--target-dir", "generated"))
}