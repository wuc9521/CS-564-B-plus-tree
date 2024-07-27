out/BTreeMain.class: src/*.java
	@javac -cp .:src -d out src/BTreeMain.java

run: out/BTreeMain.class
	@java -cp out BTreeMain

clean: 
	@rm -f out/*.class

test: out/BTreeMain.class
	@cp archive/Student.cpy.csv src/Student.csv
	@java -cp out BTreeMain