clear;clc;

% this code likely wont work without an RK method with adaptive step sizes

% export file name
output_filename = 'test.gif';
input_filename = 'three_body_parameters.csv';
data = readtable(input_filename);

index = 1;
T = data.T0(index);

% number of loops
n = 10000;
% normalized gravitational constant
G = 1;
% timestep
dt = 1/T;
% length of trails
trail_length = 150;


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
Q1(1,:) = [data.x1(index), -data.x1(index), 0];
Q2(1,:) = [data.x2(index), -data.x2(index), 0];
Pq1(1,:) = [data.v1(index), data.v1(index), -2 * data.v1(index)];
Pq2(1,:) = [data.v2(index), data.v2(index), -2 * data.v2(index)];

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
			    %r = max(0.0001, r);
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
         imwrite(imind,cm,output_filename,'gif', 'Loopcount',inf);
    else
         imwrite(imind,cm,output_filename,'gif','WriteMode','append', 'DelayTime', 0.02);
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