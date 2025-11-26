import React, { useState } from 'react';
import './ModelSelector.css';

interface ModelInfo {
  id: number;
  name: string;
  description: string;
  gifUrl: string;
}

interface ModelSelectorProps {
  selectedModel: number;
  onModelSelect: (model: number) => void;
  availableModels: ModelInfo[];
}

export const ModelSelector: React.FC<ModelSelectorProps> = ({
  selectedModel,
  onModelSelect,
  availableModels,
}) => {
  const [isOpen, setIsOpen] = useState(false);

  const selectedModelInfo = availableModels.find(m => m.id === selectedModel) || availableModels[0];

  const handleModelClick = (modelId: number) => {
    onModelSelect(modelId);
    setIsOpen(false);
  };

  const handleBackdropClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      setIsOpen(false);
    }
  };

  return (
    <>
      <div className="model-selector-trigger">
        <label>Model:</label>
        <button
          type="button"
          className="model-selector-button"
          onClick={() => setIsOpen(true)}
        >
          <span className="model-selector-button-text">
            Model {selectedModelInfo.id}: {selectedModelInfo.name}
          </span>
          <span className="model-selector-button-arrow">▼</span>
        </button>
        <small>Click to select a dropdown template model</small>
      </div>

      {isOpen && (
        <div className="model-selector-overlay" onClick={handleBackdropClick}>
          <div className="model-selector-popup">
            <div className="model-selector-popup-header">
              <h3>Select Dropdown Model</h3>
              <button
                type="button"
                className="model-selector-close"
                onClick={() => setIsOpen(false)}
                aria-label="Close"
              >
                ×
              </button>
            </div>
            <div className="model-selector-popup-content">
              {availableModels.map((model) => (
                <div
                  key={model.id}
                  className={`model-selector-item ${model.id === selectedModel ? 'selected' : ''}`}
                  onClick={() => handleModelClick(model.id)}
                >
                  <div className="model-selector-item-gif">
                    <img
                      src={model.gifUrl}
                      alt={`Model ${model.id}: ${model.name}`}
                      onError={(e) => {
                        // Fallback to placeholder if GIF fails to load
                        const target = e.target as HTMLImageElement;
                        target.style.display = 'none';
                        const placeholder = target.parentElement?.querySelector('.model-selector-placeholder');
                        if (placeholder) {
                          (placeholder as HTMLElement).style.display = 'flex';
                        }
                      }}
                    />
                    <div className="model-selector-placeholder" style={{ display: 'none' }}>
                      <div style={{ textAlign: 'center' }}>
                        <div style={{ fontSize: '2em', marginBottom: '8px' }}>🎬</div>
                        <span>GIF Preview</span>
                        <div style={{ fontSize: '0.8em', color: '#999', marginTop: '4px' }}>
                          Coming soon
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="model-selector-item-info">
                    <div className="model-selector-item-name">
                      Model {model.id}: {model.name}
                    </div>
                    <div className="model-selector-item-description">
                      {model.description}
                    </div>
                  </div>
                  {model.id === selectedModel && (
                    <div className="model-selector-item-check">✓</div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

