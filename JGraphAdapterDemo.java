

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public class JGraphAdapterDemo extends JApplet {
    private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension DEFAULT_SIZE = new Dimension( 530, 320 );

    // 
    private JGraphModelAdapter m_jgAdapter;

    /**
     * @see java.applet.Applet#init().
     */
    public void init(  ) {
        // create a JGraphT graph

    	//ListenableGraph g = new ListenableDirectedGraph( DefaultEdge.class );

        SimpleGraph<Integer,DefaultEdge> h = Generation.cycle(4);
        h = Generation.completeJoin(h, Generation.cycle(4, 5));
        
        
        System.out.println(Recognition.isP4Sparse(h));
        System.out.println(Recognition.isHHfree(h));
        System.out.println(Recognition.isChordal(h));
        
        
        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter( h );

        JGraph jgraph = new JGraph( m_jgAdapter );

        adjustDisplaySettings( jgraph );
        getContentPane(  ).add( jgraph );
        resize( DEFAULT_SIZE );

    }


    private void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize( DEFAULT_SIZE );

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter( "bgcolor" );
        }
         catch( Exception e ) {}

        if( colorStr != null ) {
            c = Color.decode( colorStr );
        }

        jg.setBackground( c );
    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
        Map              attr = cell.getAttributes(  );
        Rectangle        b    = (Rectangle) GraphConstants.getBounds( attr );

        GraphConstants.setBounds( attr, new Rectangle( x, y, b.width, b.height ) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null );
    }
}