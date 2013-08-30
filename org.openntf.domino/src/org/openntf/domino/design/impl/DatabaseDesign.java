/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class DatabaseDesign implements org.openntf.domino.design.DatabaseDesign {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DatabaseDesign.class.getName());
	private static final long serialVersionUID = 1L;

	/*
	 * Some handy constant Note IDs for getting specific elements. h/t http://www.nsftools.com/tips/NotesTips.htm#defaultelements
	 */
	private static final String ABOUT_NOTE = "FFFF0002";
	private static final String DEFAULT_FORM = "FFFF0004";
	private static final String DEFAULT_VIEW = "FFFF0008";
	private static final String ICON_NOTE = "FFFF0010";
	private static final String DESIGN_COLLECTION = "FFFF0020";
	private static final String ACL_NOTE = "FFFF0040";
	private static final String USING_NOTE = "FFFF0100";
	private static final String REPLICATION_FORMULA = "FFFF0800";

	private final Database database_;

	public DatabaseDesign(final Database database) {
		database_ = database;
	}

	@Override
	public DesignView createView() {
		return new DesignView(database_);
	}

	public FileResource createFileResource() {
		return new FileResource(database_);
	}

	@Override
	public AboutDocument getAboutDocument() {
		Document doc = database_.getDocumentByID(ABOUT_NOTE);
		if (doc != null) {
			return new AboutDocument(doc);
		}
		return null;
	}

	@Override
	public ACLNote getACL() {
		return new ACLNote(database_.getDocumentByID(ACL_NOTE));
	}

	@Override
	public DesignForm getDefaultForm() {
		Document formDoc = database_.getDocumentByID(DEFAULT_FORM);
		if (formDoc != null) {
			return new DesignForm(formDoc);
		}
		return null;
	}

	@Override
	public DesignView getDefaultView() {
		Document viewDoc = database_.getDocumentByID(DEFAULT_VIEW);
		if (viewDoc != null) {
			return new DesignView(viewDoc);
		}
		return null;
	}

	@Override
	public FileResource getFileResource(final String name) {
		NoteCollection notes = getNoteCollection(
				String.format(" !@Contains($Flags; '~') & @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ",
						DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new FileResource(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getFileResources() {
		NoteCollection notes = getNoteCollection(" !@Contains($Flags; '~') & @Contains($Flags; 'g') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	@Override
	public FileResource getHiddenFileResource(final String name) {
		NoteCollection notes = getNoteCollection(String.format(
				" @Contains($Flags; '~') & @Contains($Flags; 'g') & !@Contains($Flags; 'K':';':'[':',') & @Explode($TITLE; '|')=\"%s\" ",
				DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new FileResource(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.FileResource> getHiddenFileResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; '~') & @Contains($Flags; 'g') & !@Contains($Flags; 'K':';':'[':',')",
				EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.FileResource>(notes, FileResource.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResource(java.lang.String)
	 */
	@Override
	public JavaResource getJavaResource(final String name) {
		NoteCollection notes = getNoteCollection(
				String.format(" @Contains($Flags; 'g') & @Contains($Flags; '[') & @Explode($TITLE; '|')=\"%s\" ",
						DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new JavaResource(doc);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getJavaResources()
	 */
	@Override
	public DesignCollection<org.openntf.domino.design.JavaResource> getJavaResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; '[') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.JavaResource>(notes, JavaResource.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getXPage(java.lang.String)
	 */
	@Override
	public XPage getXPage(final String name) {
		NoteCollection notes = getNoteCollection(
				String.format(" @Contains($Flags; 'g') & @Contains($Flags; 'K') & @Explode($TITLE; '|')=\"%s\" ",
						DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new XPage(doc);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getXPages()
	 */
	@Override
	public DesignCollection<org.openntf.domino.design.XPage> getXPages() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; 'K') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.XPage>(notes, XPage.class);
	}

	public JarResource getJarResource(final String name) {
		NoteCollection notes = getNoteCollection(
				String.format(" @Contains($Flags; 'g') & @Contains($Flags; ',') & @Explode($TITLE; '|')=\"%s\" ",
						DominoUtils.escapeForFormulaString(name)), EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new JarResource(doc);
		}
		return null;
	}

	public DesignCollection<org.openntf.domino.design.JarResource> getJarResources() {
		NoteCollection notes = getNoteCollection(" @Contains($Flags; 'g') & @Contains($Flags; ',') ", EnumSet.of(SelectOption.MISC_FORMAT));
		return new DesignCollection<org.openntf.domino.design.JarResource>(notes, JarResource.class);
	}

	@Override
	public FileResource getAnyFileResource(final String name) {
		NoteCollection notes = getNoteCollection(
				String.format(" @Contains($Flags; 'g') & @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
				EnumSet.of(SelectOption.MISC_FORMAT));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new FileResource(doc);
		}
		return null;
	}

	@Override
	public DesignForm getForm(final String name) {
		// TODO Check if this returns subforms
		NoteCollection notes = getNoteCollection(String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
				EnumSet.of(SelectOption.FORMS));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new DesignForm(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignForm> getForms() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.FORMS));
		return new DesignCollection<org.openntf.domino.design.DesignForm>(notes, DesignForm.class);
	}

	@Override
	public IconNote getIconNote() {
		return new IconNote(database_.getDocumentByID(ICON_NOTE));
	}

	@Override
	public ReplicationFormula getReplicationFormula() {
		Document repNote = database_.getDocumentByID(REPLICATION_FORMULA);
		if (repNote != null) {
			return new ReplicationFormula(repNote);
		}
		return null;
	}

	@Override
	public UsingDocument getUsingDocument() {
		Document doc = database_.getDocumentByID(USING_NOTE);
		if (doc != null) {
			return new UsingDocument(doc);
		}
		return null;
	}

	@Override
	public org.openntf.domino.design.DesignView getView(final String name) {
		// TODO Check if this returns folders
		NoteCollection notes = getNoteCollection(String.format(" @Explode($TITLE; '|')=\"%s\" ", DominoUtils.escapeForFormulaString(name)),
				EnumSet.of(SelectOption.VIEWS));

		String noteId = notes.getFirstNoteID();
		if (!noteId.isEmpty()) {
			Document doc = database_.getDocumentByID(noteId);
			return new DesignView(doc);
		}
		return null;
	}

	@Override
	public DesignCollection<org.openntf.domino.design.DesignView> getViews() {
		NoteCollection notes = getNoteCollection(" @All ", EnumSet.of(SelectOption.VIEWS));
		return new DesignCollection<org.openntf.domino.design.DesignView>(notes, DesignView.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader()
	 */
	@Override
	public ClassLoader getDatabaseClassLoader(final ClassLoader parent) {
		return new DatabaseClassLoader(parent, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DatabaseDesign#getDatabaseClassLoader(java.lang.ClassLoader, boolean)
	 */
	@Override
	public ClassLoader getDatabaseClassLoader(final ClassLoader parent, final boolean includeJars) {
		return new DatabaseClassLoader(parent, includeJars);
	}

	private NoteCollection getNoteCollection(final String selectionFormula, final Set<SelectOption> options) {
		NoteCollection notes = database_.createNoteCollection(false);
		notes.setSelectOptions(options);
		notes.setSelectionFormula(selectionFormula);
		notes.buildCollection();
		return notes;
	}

	class DatabaseClassLoader extends ClassLoader {
		private final Map<String, byte[]> unloadedClasses_ = new HashMap<String, byte[]>();
		private final boolean includeJars_;

		public DatabaseClassLoader(final ClassLoader parent, final boolean includeJars) {
			super(parent);
			includeJars_ = includeJars;
		}

		@Override
		protected Class<?> findClass(final String name) throws ClassNotFoundException {
			// Check if it's in our pool of in-process classes
			if (unloadedClasses_.containsKey(name)) {
				byte[] classData = unloadedClasses_.remove(name);
				return defineClass(name, classData, 0, classData.length);
			}

			// Check for appropriate design elements in the DB
			NoteCollection notes = getNoteCollection(String.format(
					" @Contains($Flags; 'g') & (@Contains($Flags; '[') | @Contains($Flags; 'K')) & $ClassIndexItem='WEB-INF/classes/%s' ",
					DominoUtils.escapeForFormulaString(binaryNameToFilePath(name, "/"))), EnumSet.of(SelectOption.MISC_FORMAT));
			String noteId = notes.getFirstNoteID();
			if (!noteId.isEmpty()) {
				Document doc = database_.getDocumentByID(noteId);
				JavaResource res = new JavaResource(doc);
				// Load up our class queue with the data
				unloadedClasses_.putAll(res.getClassData());
				// Now attempt to load the named class
				byte[] classData = unloadedClasses_.remove(name);
				return defineClass(name, classData, 0, classData.length);
			}

			// If we're here, see if we should look through the Jars - load them all now
			if (includeJars_) {
				DesignCollection<org.openntf.domino.design.JarResource> jars = getJarResources();
				for (org.openntf.domino.design.JarResource jar : jars) {
					try {
						readJarClasses(jar);
					} catch (IOException e) {
						DominoUtils.handleException(e);
					}
				}

				if (unloadedClasses_.containsKey(name)) {
					byte[] classData = unloadedClasses_.remove(name);
					return defineClass(name, classData, 0, classData.length);
				}
			}

			return super.findClass(name);
		}

		private void readJarClasses(final org.openntf.domino.design.JarResource jar) throws IOException {
			byte[] jarData = jar.getFileData();

			ByteArrayInputStream bis = new ByteArrayInputStream(jarData);
			JarInputStream jis = new JarInputStream(bis);
			JarEntry entry = jis.getNextJarEntry();
			Map<String, byte[]> classData = new HashMap<String, byte[]>();
			while (entry != null) {
				String name = entry.getName();
				if (name.endsWith(".class")) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while (jis.available() > 0) {
						bos.write(jis.read());
					}
					classData.put(filePathToBinaryName(name, "/"), bos.toByteArray());
				}

				entry = jis.getNextJarEntry();
			}
			jis.close();

			unloadedClasses_.putAll(classData);
		}
	}

	public static String binaryNameToFilePath(final String binaryName, final String separator) {
		return binaryName.replace(".", separator) + ".class";
	}

	public static String filePathToBinaryName(final String filePath, final String separator) {
		return filePath.substring(0, filePath.length() - 6).replace(separator, ".");
	}
}