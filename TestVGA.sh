#riscv64-unknown-elf-gcc -march=rv32i -mabi=ilp32 ../SoC_project/tests/$(file).c -o ../SoC_project/out/$(file).out -Os -nostartfiles -nodefaultlibs -nostdlib

cd ../SoC_project
./test.sh ${1-win}
cd ../wildcat
sbt "testOnly wildcat.ownTests.TestVgaSimple"
