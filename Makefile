out/BTreeMain.class: src/BTreeMain.java
	@javac -cp .:src -d out src/BTreeMain.java

run: out/BTreeMain.class
	@java -cp out BTreeMain

clean: 
	@rm -f out/*.class