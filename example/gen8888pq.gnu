reset; set term win
load 'gen8888.log'
set title "analysis #8888"
set xlabel "Q (Angs.^-1)"
set ylabel "dS/dO(Q)"
set log y ; p  'gen888801.fit' u 1:2:($2*10**(-rappm($6/$2)/log(10))):($2*10**(rappm($6/$2)/log(10))) t 'Ne=  1'  w err, 'gen888801.pq' u 1:(k01*$2+b01) notit w l lt 3 lw 3; pa -1 
