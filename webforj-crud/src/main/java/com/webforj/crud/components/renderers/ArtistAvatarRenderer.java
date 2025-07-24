package com.webforj.crud.components.renderers;

import com.webforj.component.table.renderer.Renderer;
import com.webforj.crud.entity.MusicArtist;

/**
 * Simple avatar renderer for displaying music artists with circular avatars and formatted info.
 */
public class ArtistAvatarRenderer extends Renderer<MusicArtist> {
    
    @Override
    public String build() {
        return /* html */"""
            <%
            const artist = cell.value;
            const name = cell.row.getValue("Name");
            const yearFormed = cell.row.getValue("Year Formed");
            const isActive = cell.row.getValue("Active");
            
            // Generate initials (simple version)
            const initials = name ? name.split(' ').map(word => word.charAt(0)).join('').substring(0, 2).toUpperCase() : '?';
            
            // Generate year text
            const yearText = yearFormed ? (isActive ? 'Active since ' + yearFormed : 'Formed in ' + yearFormed) : 'Status unknown';
            %>
            <div part='artist-avatar-renderer'>
                <div part='artist-avatar'>
                    <%= initials %>
                </div>
                <div part='artist-text'>
                    <div part='artist-name'><%= name %></div>
                    <div part='artist-year'><%= yearText %></div>
                </div>
            </div>
            """;
    }
}