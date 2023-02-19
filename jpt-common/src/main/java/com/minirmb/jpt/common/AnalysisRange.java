package com.minirmb.jpt.common;


import java.util.List;

public class AnalysisRange {

	private List<AnalysisRangeItem> exclude;
	private List<AnalysisRangeItem> include;

	public List<AnalysisRangeItem> getExclude() {
		return exclude;
	}

	public void setExclude(List<AnalysisRangeItem> exclude) {
		this.exclude = exclude;
	}

	public List<AnalysisRangeItem> getInclude() {
		return include;
	}

	public void setInclude(List<AnalysisRangeItem> include) {
		this.include = include;
	}

	private String[] splitPackageAndClass(String classNameWithPackageName){
		int lastDot = classNameWithPackageName.lastIndexOf(".");
		final String packageName = classNameWithPackageName.substring(0, lastDot);
		final String clazzName = classNameWithPackageName.substring(lastDot+1);
		return new String[]{packageName, clazzName};
	}

	public boolean isInIncludeRange( String fullClassName ){
		String[] names = splitPackageAndClass(fullClassName.replace("/","."));
		final String pName = names[0];
		final String cName = names[1];
		return include.stream().anyMatch( ari -> ari.isPackageInRange( pName ) && ari.isClassInRange( cName ));
	}

	public boolean shouldAnalysisMethod( String fullClassName, String methodName ){
		boolean result = false;
		String[] names = splitPackageAndClass(fullClassName.replace("/","."));
		final String pName = names[0];
		final String cName = names[1];
		boolean shouldExclude = exclude.stream().anyMatch( ari ->
			 ari.isPackageInRange(pName) && ari.isClassInRange(cName) && ari.isMethodInRange(methodName)
		);

		if( !shouldExclude ){
			result= include.stream().anyMatch( ari ->
				ari.isPackageInRange( pName ) && ari.isClassInRange( cName ) && ari.isMethodInRange(methodName)
			);
		}
		return result;
	}
}
