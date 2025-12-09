run: all
	java -Djava.util.logging.config.file=src/main/resources/logging.properties \
	-cp "out:src/main/java/com/simpfi/lib/*" com.simpfi.App
all:
	javac -cp "src/main/java/com/simpfi/lib/*" -d out \
    src/main/java/com/simpfi/*.java \
    src/main/java/com/simpfi/config/*.java \
    src/main/java/com/simpfi/object/*.java \
    src/main/java/com/simpfi/sumo/wrapper/*.java \
    src/main/java/com/simpfi/ui/*.java \
    src/main/java/com/simpfi/ui/panel/*.java \
    src/main/java/com/simpfi/util/*.java \
    src/main/java/com/simpfi/util/reader/*.java
