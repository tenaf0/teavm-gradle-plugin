package hu.garaba;

import java.io.File;
import java.net.URLClassLoader;
import java.util.List;

import org.gradle.api.GradleException;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;
import org.teavm.tooling.TeaVMProblemRenderer;
import org.teavm.tooling.TeaVMToolLog;
import org.teavm.tooling.builder.BuildException;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.InProcessBuildStrategy;

abstract public class TeaVMTask extends AbstractCompile {
	@Input
	abstract public Property<TeaVMExtension> getSettings();

	@Input
	abstract public Property<String> getCacheDirectory();

	private final TeaVMToolLog teaLog = createLogger();

	@TaskAction
	void task() {
		try {
			InProcessBuildStrategy builder = new InProcessBuildStrategy(URLClassLoader::new);
			builder.init();

			List<String> classpath = getClasspath().getFiles().stream().map(File::getAbsolutePath).toList();
			getLogger().info("TeaVM plugin uses the following classpath: {}", classpath);

			builder.setClassPathEntries(classpath);
			builder.setTargetDirectory(getDestinationDirectory().get().toString());
			builder.setIncremental(true);
			builder.setCacheDirectory(getCacheDirectory().get());

			ExtensionMapper.INSTANCE.builderFromExtension(getSettings().get(), builder);

			BuildResult result = builder.build();
			TeaVMProblemRenderer.describeProblems(result.getCallGraph(), result.getProblems(), teaLog);
			if (result.getProblems().getProblems().size() > 0) {
				throw new GradleException("TeaVM compile failed");
			}
		}
		catch (BuildException e) {
			throw new RuntimeException(e);
		}
	}

	private TeaVMToolLog createLogger() {
		return new TeaVMToolLog() {
			@Override
			public void info(String text) {
				getLogger().info(text);
			}

			@Override
			public void debug(String text) {
				getLogger().debug(text);
			}

			@Override
			public void warning(String text) {
				getLogger().warn(text);
			}

			@Override
			public void error(String text) {
				getLogger().error(text);
			}

			@Override
			public void info(String text, Throwable e) {
				getLogger().info(text, e);
			}

			@Override
			public void debug(String text, Throwable e) {
				getLogger().debug(text, e);
			}

			@Override
			public void warning(String text, Throwable e) {
				getLogger().warn(text, e);
			}

			@Override
			public void error(String text, Throwable e) {
				getLogger().error(text, e);
			}
		};
	}
}
