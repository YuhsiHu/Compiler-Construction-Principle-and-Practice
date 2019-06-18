# Compiler-Construction-Principle-and-Practice
Exercises about Compiler Construction in NPU and a simplified C compiler. 

## Usage 
将单个工程文件夹导入到eclipse中，中间步骤的文档和测试方案在每个文件夹下的word文档中。
## Exercise 1 词法分析程序(Lexical Analysis) 
具体要求及分析见[Lexical Analysis](https://github.com/YuhsiHu/Compiler-Construction-Principle-and-Practice/tree/master/Lexical%20Analysis)

## Exercise 2 语法分析程序(Gramma Analysis) 
具体要求及分析见[Gramma Analysis](https://github.com/YuhsiHu/Compiler-Construction-Principle-and-Practice/tree/master/Gramma%20Analysis)

## C语言子集编译器(Simplified C compiler) 
小型C语言编译器，文法是C语言的子集，能将C语言编译为汇编语言，并且将词法分析结果、语法分析步骤、中间代码和汇编语言分别输出到txt文档中。

### **LL(1)文法:**   

>**1. 文法开始：**   
* S->void main(){A}     
>**2. 声明：**   
* X->YZ;   
* Y->int|char|bool   
* Z->UZ’   
* Z’->,Z|$  
* U−>idU′  
* U′−>=L|$   
>**3. 赋值：**  
* R->id=L;   
>**4. 算术运算：**   
* L->TL’   
* L’->+L|-L|$  
* T−>FT′  
* T′−>∗T|/T|$     
* F->(L)     
* F->id|num     
* O->++|–-|$  
* Q−>idO|$     
>**5. 布尔运算:**   
* E->HE’   
* E’->&&E|$  
* H−>GH′  
* H′−>||H  
* H′−>$ 
* G->FDF   
* D-><|>|==|!=   
* G->(E)   
* G->!E   
>**6. 控制语句:**   
* B->if (E){A}else{A}   
* B->while(E){A}   
* B->for(YZ;G;Q){A}   
>**7. 功能函数:**   
* B->printf(P);   
* B->scanf(id);   
* P->id|ch|num  
>**8. 复合语句:**   
* A->CA   
* C->X|B|R   
* A->$  

### **构造LL(1)属性翻译文法**  
构造LL1属性翻译文法即在原有LL1文法基础上加上动作符号，并给非终结符和终结符加上一定属性，给动作符号加上语义子程序。  
  
**1.赋值**   
产生式 | 语义子程序
----- | --------
R->@ASS_R id =L@ EQ; | @ASS_R{R.VAL=id并压入语义栈}
同上 | @EQ{RES=R.VAL, OP=’=’, ARG1=L.VAL, new FourElement(OP,ARG1,/, RES)}
U->@ASS_U idU’ | @ASS_U{U.VAL=id并压入语义栈}
U’ -> =L&#124;$@EQ_U’ | @EQ_U'{RES=U.VAL, OP=’=’, ARG1=L.VAL, new FourElement(OP,ARG1,/, RES)}

**2.算术运算** 
产生式 | 语义子程序
----- | --------  
L->TL’ | @ADD_SUB {If(OP!=null) RES= NEWTEMP( ); L.VAL=RES,并压入语义栈;New FourElement(OP, T.VAL, L’.VAL, RES),}
L’->+L | @ADD {OP=+, ARG2=L.VAL}   
T->FT’ | @DIV_MUL { if (OP !=null) RES= NEWTEMP( ); T.VAL=RES; new FourElement(OP,F.VAL,ARG2, RES) else ARG1=F.VAL; } 
T’->/T | @DIV {OP=/, ARG2=T.VAL} 
T’->*T | @MUL {OP=&#215;, ARG2=T.VAL} 
O-> @SINGLE_OP ++&#124;--&#124;$|@SINGLE_OP{OP=++&#124;–-}

**3.布尔运算** 
产生式 | 语义子程序
----- | --------  
G->FDF | @COMPARE{OP=D.VAL; ARG1=F(1).VAL; ARG2=F(2).VAL, RES=NEWTEMP( ); new FourElement(OP,F.VAL,ARG2,RES); G.VAL=RES并压入语义栈}
D-><&#124;>&#124;==&#124;!= | @COMPARE_OP<&#124;>&#124;==&#124;!={D.VAL=<&#124;>&#124;==&#124;!=并压入语义栈} 

**4.控制语句**  
产生式 | 语义子程序
----- | --------  
B->if (G)@IF_FJ{A}@IF_BACKPATCH_FJ @IF_RJ else{A}@IF_BACKPATCH_RJ | @IF_FJ{OP=”FJ”;ARG1=G.VAL;RES=if_fj, New FourElement(OP,ARG1,/, RES ),将其插入到四元式列表中第i个} @IF_BACKPATCH_FJ{回填前面假出口跳转四元式的跳转序号, BACKPATCH (i,if_fj)} 
B->while(G)@WHILE_FJ{A}@WHILE_RJ@WHILE_BACKPATCH_FJ |  {参照if-else}  
B->for(YZ;G@FOR_FJ;Q){A@SINGLE}@FOR_RJ@FOR_BACKPATCH_FJ  | {参照if-else} @SINGLE {ARG1=id;RES=NEWTEMP( );New FourElement(OP,ARG1,/,RES)}  

> **说明：**  
（1）R.VAL表示符号R的值，VAL是R的一个属性，其它的类似。 
（2）NEWTEMP()函数：每调用一次生成一个临时变量，依次为T1，T2 ，… ，Tn。  
（3）BACKPATCH(int i,int res):回填函数，用res回填第i个四元式的跳转地址。   
（4）New fourElement(String OP,String ARG1,String ARG2,String RES):生成一个四元式 (OP,ARG1,ARG2,RES)。

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[GPL-3.0](https://github.com/YuhsiHu/Compiler-Construction-Principle-and-Practice/blob/master/LICENSE.md)