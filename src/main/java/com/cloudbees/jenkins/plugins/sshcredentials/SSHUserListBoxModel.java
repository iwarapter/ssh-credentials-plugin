package com.cloudbees.jenkins.plugins.sshcredentials;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * {@link ListBoxModel} with {@link SSHUser} support.
 *
 * <p>
 * This class is convenient for providing the config.groovy/.jelly fragment for a collection of {@link SSHUser} objects.
 *
 * <p>
 * If you want to let the user configure an {@link SSHUser} object, do the following:
 *
 * <p>
 * First, create a field that stores the credential ID and defines a corresponding parameter in the constructor:
 *
 * <pre>
 * private String credentialId;
 *
 * &#64;DataBoundConstructor
 * public MyModel( .... , String credentialId) {
 *     this.credentialId = credentialId;
 *     ...
 * }
 * </pre>
 *
 * <p>
 * Your <tt>config.groovy</tt> should have the following entry to render a drop-down list box:
 *
 * <pre>
 * f.entry(title:_("Credentials"), field:"credentialId") {
 *     f.selecct()
 * }
 * </pre>
 *
 * <p>
 * Finally, your {@link Descriptor} implementation should have the <tt>doFillCredentialsIdItems</tt> method, which
 * lists up the credentials available in this context:
 *
 * <pre>
 * public SSHUserListBoxModel doFillCredentialsIdItems() {
 *     SSHUserListBoxModel r = new SSHUserListBoxModel();
 *     r.addCollection(CredentialsProvider.lookupCredentials(SSHUser.class,...)); // populate 'r'
 *     return r;
 * }
 * </pre>
 *
 * <p>
 * Exactly which overloaded version of the {@link CredentialsProvider#lookupCredentials(Class)} depends on
 * the context in which your model operates.
 *
 * @author Kohsuke Kawaguchi
 */
public class SSHUserListBoxModel extends ListBoxModel {
    public SSHUserListBoxModel add(SSHUser u) {
        add(u.getUsername() + (StringUtils.isNotEmpty(u.getDescription()) ? " (" + u.getDescription() + ")" : ""), u.getId());
        return this;
    }

    public SSHUserListBoxModel addCollection(Collection<? extends SSHUser> col) {
        for (SSHUser u : col)
            add(u);
        return this;
    }

    /**
     * Adds all the system-scoped credentials.
     *
     * <p>
     * These credentials are meant to be used for system configuration and other things scoped to the {@link Jenkins} object,
     * such as slaves.
     */
    public SSHUserListBoxModel addSystemScopeCredentials() {
        return addCollection(CredentialsProvider.lookupCredentials(SSHUser.class));
    }
}