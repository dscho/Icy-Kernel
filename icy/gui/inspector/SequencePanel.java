/*
 * Copyright 2010-2013 Institut Pasteur.
 * 
 * This file is part of Icy.
 * 
 * Icy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Icy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Icy. If not, see <http://www.gnu.org/licenses/>.
 */
package icy.gui.inspector;

import icy.gui.component.PopupPanel;
import icy.gui.main.ActiveSequenceListener;
import icy.gui.main.ActiveViewerListener;
import icy.gui.sequence.SequenceInfosPanel;
import icy.gui.viewer.Viewer;
import icy.gui.viewer.ViewerEvent;
import icy.sequence.Sequence;
import icy.sequence.SequenceEvent;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * @author Stephane
 */
public class SequencePanel extends JPanel implements ActiveSequenceListener, ActiveViewerListener
{
    /**
     * 
     */
    private static final long serialVersionUID = -5727785928741370159L;

    private final PopupPanel canvasPopupPanel;
    private final PopupPanel lutPopupPanel;
    private final PopupPanel infosPopupPanel;

    private final JPanel canvasPanel;
    private final JPanel lutPanel;
    private final JPanel infosPanel;

    private final SequenceInfosPanel sequenceInfosPanel;

    /**
     * 
     */
    public SequencePanel()
    {
        super();

        // enable vertical scroll only
        // setScrollableTracksViewportHeight(false);
        // setScrollableTracksViewportWidth(true);

        canvasPopupPanel = new PopupPanel("Canvas");
        canvasPanel = canvasPopupPanel.getMainPanel();
        canvasPanel.setLayout(new BorderLayout());
        canvasPopupPanel.expand();
        lutPopupPanel = new PopupPanel("Lookup Table");
        lutPanel = lutPopupPanel.getMainPanel();
        lutPanel.setLayout(new BorderLayout());
        lutPopupPanel.expand();
        infosPopupPanel = new PopupPanel("Sequence Properties");
        infosPanel = infosPopupPanel.getMainPanel();
        infosPanel.setLayout(new BorderLayout());
        infosPopupPanel.expand();

        sequenceInfosPanel = new SequenceInfosPanel();
        infosPanel.add(sequenceInfosPanel, BorderLayout.CENTER);

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));

        topPanel.add(canvasPopupPanel);
        topPanel.add(lutPopupPanel);
        topPanel.add(infosPopupPanel);

        topPanel.validate();

        setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(Box.createGlue(), BorderLayout.CENTER);

        validate();
    }

    public void setCanvasPanel(JPanel panel)
    {
        canvasPanel.removeAll();

        if (panel != null)
            canvasPanel.add(panel, BorderLayout.CENTER);

        canvasPanel.revalidate();
        // we need it for zoom value refresh in detached mode
        // FIXME : normally revalidate should be enough
        canvasPanel.repaint();
    }

    public void setLutPanel(JPanel panel)
    {
        lutPanel.removeAll();

        if (panel != null)
            lutPanel.add(panel, BorderLayout.CENTER);

        lutPanel.revalidate();
        // we need it for histogram refresh in detached mode
        // FIXME : normally revalidate should be enough
        lutPanel.repaint();
    }

    @Override
    public void viewerActivated(Viewer viewer)
    {
        if (viewer != null)
        {
            setLutPanel(viewer.getLutViewer());
            setCanvasPanel(viewer.getCanvasPanel());
        }
        else
        {
            setLutPanel(null);
            setCanvasPanel(null);
        }
    }

    @Override
    public void viewerDeactivated(Viewer viewer)
    {
        // nothing here
    }

    @Override
    public void activeViewerChanged(ViewerEvent event)
    {
        // we receive from current focused viewer only
        switch (event.getType())
        {
            case CANVAS_CHANGED:
                // refresh canvas panel
                setCanvasPanel(event.getSource().getCanvasPanel());
                break;

            case LUT_CHANGED:
                // refresh lut panel
                setLutPanel(event.getSource().getLutViewer());
                break;

            case POSITION_CHANGED:
                // nothing to do
                break;
        }
    }

    @Override
    public void sequenceActivated(Sequence sequence)
    {
        sequenceInfosPanel.sequenceActivated(sequence);
    }

    @Override
    public void sequenceDeactivated(Sequence sequence)
    {
        // nothing here
    }

    @Override
    public void activeSequenceChanged(SequenceEvent event)
    {
        sequenceInfosPanel.activeSequenceChanged(event);
    }

}
