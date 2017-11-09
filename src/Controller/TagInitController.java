package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JTextArea;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import UI.MainComponent;

public class TagInitController {
	public static String DEFAULT_EXT_CONFIG = "extconfig.txt";
	public static String DEFAULT_IMPORT_CONFIG = "importconfig.txt";
	public static String DEFAULT_OUTPUT_CONFIG = "Output";

	private ExtFilter extfiler = new ExtFilter(DEFAULT_EXT_CONFIG);
	private ImportFilter importfilter = new ImportFilter(DEFAULT_IMPORT_CONFIG);

	private File outputdir = new File(DEFAULT_OUTPUT_CONFIG);
	private BufferedWriter bfw;
	private MainComponent component;
	private int filecount;
	
	private ArrayList<File> sourcefiles=new ArrayList<>();
	private ArrayList<String> sourcepath=new ArrayList<>();
 
	private static TagInitController controller = new TagInitController();

	public static TagInitController getInstance() {
		return controller;
	}

	private TagInitController() {
		filecount = 0;
		if (!outputdir.exists()) {
			outputdir.mkdirs();
		}
	}

	public void initImportFilter(String path) {
		importfilter.init(path);
	}

	public void initOutputDir(File path) {
		if (path.exists() && path.isDirectory()) {
			outputdir = path;
		}
	}

	public void register(MainComponent component) {
		this.component = component;
	}

	public MainComponent getComponent() {
		return component;
	}

	public void initProject(File projroot) {
		try {
			// configure output
			sourcefiles.clear();
			sourcepath.clear();
			filecount=0;
			File output = new File(outputdir, projroot.getName()+".txt");
			FileWriter fw = new FileWriter(output, false);
			bfw = new BufferedWriter(fw);

			File dir = projroot;

			File[] files = dir.listFiles();
			for (File elem : files) {
				if (elem.isDirectory()) {
					initPackage(elem, "/");
				} else if (elem.isFile() && extfiler.contain(elem.getName()) == ExtType.JAVA) {
					sourcefiles.add(elem);
					sourcepath.add("/"+elem.getName());
				}
			}
			
			component.setFileCount(sourcefiles.size());
			
			for(int i=0;i<sourcefiles.size();i++){
				initSourceFile(sourcefiles.get(i), sourcepath.get(i));
			}
			
			
			bfw.flush();
			bfw.close();
			component.addError("Parser Complete.");
		} catch (Exception e) {
			for(StackTraceElement element:e.getStackTrace()){
				component.addError(element.toString());
			}
		}
	}

	public void initPackage(File dir, String parentpath) {
		File[] files = dir.listFiles();
		parentpath = parentpath + dir.getName() + "/";
		for (File elem : files) {
			if (elem.isDirectory()) {
				initPackage(elem, parentpath);
			} else if (elem.isFile() && extfiler.contain(elem.getName()) == ExtType.JAVA) {
				sourcefiles.add(elem);
				sourcepath.add(parentpath+"/"+elem.getName());
			}
		}
	}

	public void initSourceFile(File file, String path) {
		try {
			// scan the source file
			BufferedReader bfr = new BufferedReader(new FileReader(file));
			String line = "";
			StringBuffer buffer = new StringBuffer();
			while ((line = bfr.readLine()) != null) {
				buffer.append(line);
			}

			// parser
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(buffer.toString().toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			Visitor visitor = new Visitor();
			cu.accept(visitor);

			// imports filter
			HashSet<String> importset = new HashSet<>();
			ArrayList<ImportDeclaration> importlist = visitor.getImports();
			for (ImportDeclaration imp : importlist) {
				String val=imp.getName().toString();
				String str = importfilter.contain(val);
				if (str != null) {
					importset.add(str);
				}
			}

			// output result
			filecount++;
			bfw.write("#" + filecount);
			bfw.newLine();
			bfw.write(path);
			bfw.newLine();
			for (String item : importset) {
				bfw.write(item);
				bfw.newLine();
			}
			component.addLog(path + " end.");
		} catch (Exception e) {
			// send error message
			component.addError(path+" parser error");
			for(StackTraceElement element:e.getStackTrace()){
				component.addError(element.toString());
			}
		}
	}

	public void initClass() {

	}

}
