package net.amygdalum.tanteemmas.testrecorder;

import static java.util.Arrays.asList;

import java.nio.file.Paths;
import java.util.List;

import net.amygdalum.testrecorder.DefaultTestRecorderAgentConfig;
import net.amygdalum.testrecorder.ScheduledTestGenerator;
import net.amygdalum.testrecorder.SnapshotConsumer;
import net.amygdalum.testrecorder.profile.Classes;
import net.amygdalum.testrecorder.profile.Methods;

public class AgentConfig extends DefaultTestRecorderAgentConfig {

	@Override
	public SnapshotConsumer getSnapshotConsumer() {
		return new ScheduledTestGenerator()
			.withDumpMaximum(1000)
			.withDumpOnShutDown(true)
			.withDumpOnCounterInterval(5)
			.withClassName("${class}${counter}Test")
			.withDumpTo(Paths.get("target/generated"));
	}
	
	@Override
	public long getTimeoutInMillis() {
		return 100_000;
	}

	@Override
	public List<Classes> getClasses() {
		return asList(Classes.byPackage("net.amygdalum.tanteemmas"), Classes.byName("java.io.Writer"));
	}
	
	@Override
	public List<Methods> getOutputs() {
		return asList(Methods.byName("write"));
	}
	
}
