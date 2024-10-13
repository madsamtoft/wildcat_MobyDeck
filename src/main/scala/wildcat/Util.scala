/*
 * Copyright (c) 2016, DTU
 * Simplified BSD License
 */

/*
 * Utility functions for the ISA simulator and pipeline of RISC-V.
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */

package wildcat

import java.io.File
import java.nio.file.{Files, Paths}
import scala.io.Source
import net.fornwall.jelf.ElfFile

object Util {


  private def byteToWord(byteArray: Array[Byte]) = {
    val arr = new Array[Int](math.max(1, byteArray.length / 4))

    if (byteArray.length == 0) {
      arr(0) = 0
    }

    // little endian
    for (i <- 0 until byteArray.length / 4) {
      var word = 0
      for (j <- 0 until 4) {
        word >>>= 8
        word += (byteArray(i * 4 + j).toInt & 0xff) << 24
      }
      // printf("%08x\n", word)
      arr(i) = word
    }
    arr
  }
  /**
   * Read a binary file into an array vector
   */
  def readBin(fileName: String): Array[Int] = {
    val byteArray = Files.readAllBytes(Paths.get(fileName))
    byteToWord(byteArray)
  }

  /**
   * Read a hex file in generated by RISC-V tests.
   * File is in a strange format: 128 bits per line,
   * resulting in first word in last (rightmost position).
   */
  def readHex(fileName: String): Array[Int] = {

    // println("Reading " + fileName)
    val length = Source.fromFile(fileName).getLines().length
    val arr = new Array[Int](length * 4)

    val lines = Source.fromFile(fileName).getLines()
    var i = 0
    for (l <- lines) {
      for (j <- 0 until 4) {
        val s = l.substring((3 - j) * 8, (3 - j) * 8 + 8)
        // Integer parsing does not like hex strings with MSB set
        arr(i * 4 + j) = java.lang.Long.parseLong(s, 16).toInt
      }
      i += 1
    }

    arr
  }

  def readElf(fileName: String): Array[Int] = {
    val elf = ElfFile.from(new File(fileName))
    if (!elf.is32Bits() || elf.e_machine != 0xf3) throw new Exception("Not a RV32I executable")
    val section = elf.firstSectionByName(".text")
    val data = section.getData
    // println(s"program start ${elf.e_entry}")
    // println(s"start of .text ${section.header.sh_addr}")
    byteToWord(data)
  }

  def getCode(name: String): (Array[Int], Int) = {
    val (code, start) =
      if (name.endsWith(".bin")) {
        (Util.readBin(name), 0)
      } else if (name.endsWith(".out")) {
        (Util.readElf(name), 0)
      } else if (name.endsWith(".hex")) {
        (Util.readHex(name), 0x200)
      } else {
        throw new Exception("Unknown file extension")
      }
    // code.foreach(x => println(f"$x%08x"))
    (code, start)
  }

  def getAsmFiles(path: String = "asm", ext: String = ".s") = {
    new File(path).listFiles.filter(_.isFile).toList.filter(_.getName.endsWith(ext)).map(_.toString)
  }

  def getSimpleTests(path: String) = {
    new File(path).listFiles.filter(_.isFile).toList.filter(_.getName.endsWith(".bin"))
  }
}
