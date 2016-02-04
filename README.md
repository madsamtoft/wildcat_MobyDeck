# Wildcat

An implementation of RISC-V.

This repository currently contains an ISA simulator of the RISC-V instruction
set. Concrete the 32-bit integer version.

Minimal SW prerequisites are a version of git and sbt.

To start with wildcat either fork the project or clone it from here with:

    $ git clone https://github.com/schoeberl/wildcat
    $ cd wildcat

Here you can start the ISA simulator executing a simple program with

    $ sbt "run-main wildcat.isasim.SimRV bin/test.bin"

That command will execute the already assembled assembler program asm/test.S
as bin/test.bin and print out a register dump for each instruction.

To assemble other programs or compile C programs you need to install
the [RISC-V tools](https://github.com/riscv/riscv-tools).

## Notes

Why wildcat? The day before starting this project I was running
in the Wildcat Canyon in Tilden park. Very nice area.
