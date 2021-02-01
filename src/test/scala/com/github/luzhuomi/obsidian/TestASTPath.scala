package com.github.luzhuomi.obsidian

import com.github.luzhuomi.scalangj.Lexer
import com.github.luzhuomi.scalangj.Parser._
import com.github.luzhuomi.scalangj.Syntax._
import com.github.luzhuomi.obsidian._ 
import com.github.luzhuomi.obsidian.ASTPath._

import org.scalatest.{FunSuite, Matchers}

class TestMethodASTPathQuery1 extends FunSuite with Matchers {
    /**
      * public static void main() {
          System.out.println("Hello World!");
        }
      */
    
    val METHOD:MemberDecl = MethodDecl(List(Public, Static),List(),None,Ident("main"),List(),List(),None,MethodBody(Some(Block(List(BlockStmt_(ExpStmt(MethodInv(MethodCall(Name(List(Ident("System"), Ident("out"), Ident("println"))),List(Lit(StringLit("Hello World!"))))))))))))
    val STATEMENT:BlockStmt = BlockStmt_(ExpStmt(MethodInv(MethodCall(Name(List(Ident("System"), Ident("out"), Ident("println"))),List(Lit(StringLit("Hello World!")))))))
    val path = List(0)
    test("TestMethodASTPathQuery1") {
        val result:Option[BlockStmt] = queryOps.query(METHOD,path)
        assert((!result.isEmpty) && (result.get == STATEMENT))
    }
}


class TestMethodASTPathQuery2 extends FunSuite with Matchers {
  val STRING = """
int get (int x) {
  int i = lpos;  // 0
  int r = -1;    // 1
  try            // 2
  {              // 2,0 a block stmt
    if (x < i)   // 2,0,0
    {            // 2,0,0,0 a block stmt
       throw  new Exception(); // 2,0,0,0,0
    } 
    else 
    {            // 2,0,0,1 a block stmt
       while (i < x)   // 2,0,0,1,0 
       {               // 2,0,1,1,0,0
          int t = f1 + f2;   // 2,0,1,1,0,0,0
          f1 = f2;           // 2,0,1,1,0,0,1
          f2 = t;            // 2,0,1,1,0,0,2
          i = i + 1;         // 2,0,1,1,0,0,3 
       }
       lpos = i;       // 2,0,0,1,1
       r = f2;         // 2,0,0,1,2
    }
  } 
  catch (Exception e)
  {              // 2,1 a block stmt
     println("..."); // 2,1,0
  }
  return r;      // 3 
}
  """
  val decl:Decl = classBodyStatement.apply(new Lexer.Scanner(STRING)).get.get
  test("TestMethodASTPathQuery2") {
      decl match {
          case InitDecl(is_static, blk) => assert(1!=1)
          case MemberDecl_(methodDecl@MethodDecl(_,_,_,_,_,_,_,_)) => {
              val path = List(0)
              assert(1==1)
          } 
          case _ => assert(1!=1)
      }
  }
}