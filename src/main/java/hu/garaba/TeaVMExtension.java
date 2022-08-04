package hu.garaba;

import java.io.Serializable;

public class TeaVMExtension implements Serializable {
	private String mainClass;
	private String optimizationLevel = "SIMPLE";
	private String targetType = "JAVASCRIPT";
	private String entryPointName = "main";

	private boolean obfuscated = true;

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public String getOptimizationLevel() {
		return optimizationLevel;
	}

	public void setOptimizationLevel(String optimizationLevel) {
		this.optimizationLevel = optimizationLevel;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getEntryPointName() {
		return entryPointName;
	}

	public void setEntryPointName(String entryPointName) {
		this.entryPointName = entryPointName;
	}

	public boolean isObfuscated() {
		return obfuscated;
	}

	public void setObfuscated(boolean obfuscated) {
		this.obfuscated = obfuscated;
	}
}
