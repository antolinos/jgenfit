reset; set term win
set title "analysis #9090"
set xlabel "Q (Angs.^-1)"
set ylabel "dS/dO(Q)";rappm(x)=x<=2.5? x:2.5
set log y ; p  'gen909001.fit' u 1:2:($2*10**(-rappm($6/$2)/log(10))):($2*10**(rappm($6/$2)/log(10))) t 'Ne=  1'  w err,'' u 1:4 notit w l lt 3 lw 3; pa -1 
set log y; plot 'gen909001.fit' u 1:4 notit w l lt 3 lw 3 ,'' u 1:2:($2*10**(-rappm($6/$2)/log(10))):($2*10**(rappm($6/$2)/log(10))) t 'Ne=  1'  w e lt 1; pause -1
set xlabel "r (Angs.)"
set ylabel "p(r)"
set nolog y; plot [][]'gen909001.pr' u 1:2 t 'Ne=  1' w l; pause -1
