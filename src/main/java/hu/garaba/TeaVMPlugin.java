package hu.garaba;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.tasks.compile.JavaCompile;

public class TeaVMPlugin implements Plugin<Project> {
	private TeaVMExtension settings;

	private final ProjectLayout layout;

	@Inject
	public TeaVMPlugin(ProjectLayout layout) {
		this.layout = layout;
	}

	@Override
	public void apply(Project project) {
		settings = new TeaVMExtension();
		project.getExtensions().add("teavm", settings);

		List<String> buildDirs = new ArrayList<>();
		List<String> classpath = new ArrayList<>();

		project.getTasks().withType(JavaCompile.class).configureEach(t -> {
			buildDirs.add(t.getDestinationDirectory().get().getAsFile().getAbsolutePath());
			classpath.add(t.getDestinationDirectory().get().getAsFile().getAbsolutePath());
		});

		FileCollection runtimeClasspath = project.getConfigurations().getByName("compileClasspath");
		runtimeClasspath.forEach(f -> classpath.add(f.getAbsolutePath()));

		project.getTasks().register("teavmc", TeaVMTask.class, task -> {
			task.getSettings().set(settings);
			task.getCacheDirectory().set(layout.getBuildDirectory().dir("tmp/teavm").get().getAsFile().getAbsolutePath());
			task.setClasspath(layout.files(classpath));
			task.setSource(buildDirs);
			task.getDestinationDirectory().set(layout.getBuildDirectory().dir("teavm"));
			task.dependsOn(project.getTasks().getByName("compileJava"));
			task.setGroup("TeaVM");
		});
	}
}
