/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-03 Wolfgang M. Meier
 *  wolfgang@exist-db.org
 *  http://exist.sourceforge.net
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id$
 */
package org.exist.dom;

import java.util.Comparator;

import org.exist.xquery.Constants;

/**
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class DocumentOrderComparator implements Comparator {

	public DocumentOrderComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		final NodeProxy p1 = (NodeProxy) o1;
		final NodeProxy p2 = (NodeProxy) o2;
		final DocumentImpl doc = p1.getDocument();
		if (doc.docId > p2.getDocument().docId)
			return 1;
		else if (doc.docId < p2.getDocument().docId)
			return Constants.INFERIOR;
		else {
			if (p1.getGID() == p2.getGID())
				return Constants.EQUAL;
			int la = doc.getTreeLevel(p1.getGID());
			int lb = doc.getTreeLevel(p2.getGID());
			if(la == lb)
				return p1.getGID() < p2.getGID() ? Constants.INFERIOR : Constants.SUPERIOR;
			long pa = p1.getGID(), pb = p2.getGID();
			if (la > lb) {
				while (la > lb) {
					pa = XMLUtil.getParentId(doc, pa, la);
					--la;
				}
				if (pa == pb)
					return Constants.SUPERIOR;
				else
					return pa < pb ? Constants.INFERIOR : Constants.SUPERIOR;
			} else {
				while (lb > la) {
					pb = XMLUtil.getParentId(doc, pb, lb);
					--lb;
				}
				if (pb == pa)
					return Constants.INFERIOR;
				else
					return pa < pb ? Constants.INFERIOR : Constants.SUPERIOR;
			}	
		}
	}

}
