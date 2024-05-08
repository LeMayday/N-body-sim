clear;clc;

% number of loops
n = 1000;
% normalized gravitational constant
G = 1;
% timestep (T ~= 6.32591398)
dt = 0.1/(6.32591398);
% length of trails
trail_length = 80;
% export file name
filename = 'test.gif';

% store all values so trails can be plotted
size = 3;
Q1 = zeros(n, 3);
Q2 = zeros(n, 3);
Pq1 = zeros(n, 3);
Pq2 = zeros(n, 3);
Fq1 = [0, 0, 0];
Fq2 = [0, 0, 0];
m = [1, 1, 1];

% initial values
% parameters from https://arxiv.org/pdf/math/0011268.pdf
Q1(1,:) = [-0.97000436, 0, 0.97000436];
Q2(1,:) = [0.24308753, 0, -0.24308753];
Pq1(1,:) = [0.4662036850, -0.93240737, 0.4662036850];
Pq2(1,:) = [0.4323657300, -0.86473146, 0.4323657300];
% perform reverse half Euler step at beginning
flag = true;

figure();

for t = 2:n
    % compute forces
    for i = 1:size
        % reset forces
        Fq1(i) = 0;
        Fq2(i) = 0;
        for j = 1:size
            if i ~= j
                distQ1 = Q1(t-1, j) - Q1(t-1, i);
			    distQ2 = Q2(t-1, j) - Q2(t-1, i);
                r = sqrt(distQ1*distQ1 + distQ2*distQ2);
			    r = max(0.0001, r);
                % calculate each component of force from each body and add
                % to total
			    Fq1(i) = Fq1(i) + G * m(j) * m(i) / (r * r * r) * distQ1;
			    Fq2(i) = Fq2(i) + G * m(j) * m(i) / (r * r * r) * distQ2;
            end
        end
    end

    if flag
        for i = 1:size
            % perform reverse half Euler step for momenta
            Pq1(t-1, i) = Pq1(t-1, i) - 0.5*Fq1(i) * dt;
		    Pq2(t-1, i) = Pq2(t-1, i) - 0.5*Fq2(i) * dt;
        end
        flag = false;
    end
    
    for i = 1:size
        % increment momenta and positions
		Pq1(t, i) = Pq1(t-1, i) + Fq1(i) * dt;
		Pq2(t, i) = Pq2(t-1, i) + Fq2(i) * dt;
		Q1(t, i) = Q1(t-1, i) + Pq1(t, i) / m(i) * dt;
		Q2(t, i) = Q2(t-1, i) +  Pq2(t, i) / m(i) * dt;			
    end
    
    % plot balls
    plot(Q1(t, 1), Q2(t, 1), 'oc', Q1(t, 2), Q2(t, 2), 'oc',...
        Q1(t, 3), Q2(t, 3), 'oc', 'MarkerSize', 4, 'MarkerFaceColor', 'c');
    
    hold on;
    % plot trails
    if t > trail_length
        plot(Q1(t - trail_length:t, 1), Q2(t - trail_length:t, 1), '-c',...
            Q1(t - trail_length:t, 2), Q2(t - trail_length:t, 2), '-c',...
            Q1(t - trail_length:t, 3), Q2(t - trail_length:t, 3), '-c');
    else
        plot(Q1(1:t, 1), Q2(1:t, 1), '-c',...
            Q1(1:t, 2), Q2(1:t, 2), '-c',...
            Q1(1:t, 3), Q2(1:t, 3), '-c');
    end
    hold off;
    
    % figure appearance
    axis([-1.5 1.5 -1 1])
    set(gca,'XColor', 'none','YColor','none');
    set(gca,'Color','k');
    set(gca,'position',[0 0 1 1],'units','normalized');

    % animation and write to file
    drawnow
    frame = getframe(gcf);
    im = frame2im(frame);
    [imind,cm] = rgb2ind(im,256);
    if t == 2
         imwrite(imind,cm,filename,'gif', 'Loopcount',inf);
    else
         imwrite(imind,cm,filename,'gif','WriteMode','append', 'DelayTime', 0.02);
    end
end

% https://www.mathworks.com/matlabcentral/answers/545447-how-to-animate-multiple-lines-at-the-same-time
% https://www.mathworks.com/help/matlab/ref/getframe.html
% https://www.mathworks.com/matlabcentral/answers/395176-how-to-plot-animation-plots
% https://www.mathworks.com/matlabcentral/answers/334964-extracting-image-from-a-figure-without-gray-border
% https://www.mathworks.com/matlabcentral/answers/166737-how-do-i-make-a-plot-background-black
% https://www.mathworks.com/matlabcentral/answers/365857-how-to-remove-axis-from-a-figure#answer_385511
% https://www.mathworks.com/help/matlab/ref/imwrite.html
% https://www.mathworks.com/matlabcentral/answers/1694690-how-to-save-figure-frames-as-gif-file#answer_942175


