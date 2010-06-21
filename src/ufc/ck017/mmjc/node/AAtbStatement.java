/* This file was generated by SableCC (http://www.sablecc.org/). */

package ufc.ck017.mmjc.node;

import ufc.ck017.mmjc.analysis.*;
import ufc.ck017.mmjc.translate.tree.Exp;

@SuppressWarnings("nls")
public final class AAtbStatement extends PStatement
{
    private TId _id_;
    private PExpression _expression_;

    public AAtbStatement()
    {
        // Constructor
    }

    public AAtbStatement(
        @SuppressWarnings("hiding") TId _id_,
        @SuppressWarnings("hiding") PExpression _expression_)
    {
        // Constructor
        setId(_id_);

        setExpression(_expression_);

    }

    @Override
    public Object clone()
    {
        return new AAtbStatement(
            cloneNode(this._id_),
            cloneNode(this._expression_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAtbStatement(this);
    }
    
    public TId getId()
    {
        return this._id_;
    }

    public void setId(TId node)
    {
        if(this._id_ != null)
        {
            this._id_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._id_ = node;
    }

    public PExpression getExpression()
    {
        return this._expression_;
    }

    public void setExpression(PExpression node)
    {
        if(this._expression_ != null)
        {
            this._expression_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expression_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._id_)
            + toString(this._expression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._id_ == child)
        {
            this._id_ = null;
            return;
        }

        if(this._expression_ == child)
        {
            this._expression_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._id_ == oldChild)
        {
            setId((TId) newChild);
            return;
        }

        if(this._expression_ == oldChild)
        {
            setExpression((PExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }

	@Override
	public int getLine() {
		return _id_.getLine();
	}

	@Override
	public int getPos() {
		return _id_.getPos();
	}
}
